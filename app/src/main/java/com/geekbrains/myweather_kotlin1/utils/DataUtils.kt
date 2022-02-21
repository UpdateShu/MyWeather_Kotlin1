package com.geekbrains.myweather_kotlin1.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.db.HistoryEntity
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.*
import com.geekbrains.myweather_kotlin1.model.forecast.dto.TimeWeatherDTO
import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getLocalDate(date: String) = LocalDate.parse(date, getDateTimeFormatter())

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(date: LocalDate) = date.format(getDateTimeFormatter())

@RequiresApi(Build.VERSION_CODES.O)
fun getDateTimeFormatter() = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@RequiresApi(Build.VERSION_CODES.O)
fun convertDtoToModel(weatherDTO: WeatherDTO) : WeekWeather? {
    val weatherForecasts : MutableList<DayWeatherForecast> = arrayListOf()
    if (weatherDTO.forecasts == null || weatherDTO.forecasts.isEmpty())
        return null

    val today = weatherDTO.forecasts[0]
    for (forecast in weatherDTO.forecasts) {
        val day = WeekDay.values()[getLocalDate(forecast.date).dayOfWeek.ordinal]

        val timeWeatherForecasts : MutableList<DayTimeWeatherForecast> = arrayListOf()
        addTimeWeatherForecast(DaysTime.Morning, forecast.parts!!.morning, timeWeatherForecasts)
        addTimeWeatherForecast(DaysTime.Day, forecast.parts!!.day, timeWeatherForecasts)
        addTimeWeatherForecast(DaysTime.Evening, forecast.parts!!.evening, timeWeatherForecasts)
        addTimeWeatherForecast(DaysTime.Night, forecast.parts!!.night, timeWeatherForecasts)

        weatherForecasts.add((DayWeatherForecast(day, timeWeatherForecasts)))
    }
    weatherDTO.forecasts[0].apply {
        return WeekWeather(getLocalDate(this.date), weatherForecasts)
    }
}

private fun addTimeWeatherForecast(daysTime: DaysTime, timeWeatherDTO : TimeWeatherDTO,
                                   timeWeatherForecasts: MutableList<DayTimeWeatherForecast>) {
    timeWeatherForecasts.add(DayTimeWeatherForecast(daysTime, Weather.fromDTO(timeWeatherDTO,
        "https://yastatic.net/weather/i/icons/funky/dark/${timeWeatherDTO.icon}.svg")))
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertTodayForecastToEntities(city: City, weekWeather: WeekWeather): MutableList<HistoryEntity> {

    val todayForecasts = weekWeather.forecasts[0]!!.timeForecasts
    val historyEntities = mutableListOf<HistoryEntity>()
    addHistoryEntity(city, weekWeather, todayForecasts[0], historyEntities)
    addHistoryEntity(city, weekWeather, todayForecasts[1], historyEntities)
    addHistoryEntity(city, weekWeather, todayForecasts[2], historyEntities)
    addHistoryEntity(city, weekWeather, todayForecasts[3], historyEntities)
    return historyEntities
}

@RequiresApi(Build.VERSION_CODES.O)
fun addHistoryEntity(city: City, weekWeather: WeekWeather, dayTimeWeatherForecast: DayTimeWeatherForecast,
                     historyEntities : MutableList<HistoryEntity>) {
    historyEntities.add(HistoryEntity(0, city.name,
        getFormattedDate(weekWeather.date),
        dayTimeWeatherForecast.daysTime.number,
        dayTimeWeatherForecast.weather.temp,
        dayTimeWeatherForecast.weather.weatherEvent.toString()))
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertHistoryEntityToForecast(city: City, entityList: List<HistoryEntity>): MutableList<HistoryForecast> {

    val historyForecasts = mutableListOf<HistoryForecast>()
    (entityList.filter { it.daysTime == 1 }).forEach( {
        val dayHistoryEntities = entityList.filter { today -> today.date == it.date }
        historyForecasts.add(HistoryForecast(city, getLocalDate(it.date), morning = it.toString(),
            day = (dayHistoryEntities.find { dt -> dt.daysTime == DaysTime.Day.number }).toString(),
            evening = (dayHistoryEntities.find { dt -> dt.daysTime == DaysTime.Evening.number }).toString(),
            night = (dayHistoryEntities.find { dt -> dt.daysTime == DaysTime.Night.number }).toString(),
        ))
    })
    return historyForecasts
}