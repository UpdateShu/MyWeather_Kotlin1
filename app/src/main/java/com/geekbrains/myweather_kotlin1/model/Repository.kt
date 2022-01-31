package com.geekbrains.myweather_kotlin1.model

import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.geekbrains.myweather_kotlin1.model.forecast.*
import com.geekbrains.myweather_kotlin1.viewmodel.AppState
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

object Repository : IRepository {

    private val _cities : ArrayList<City> by lazy {
        arrayListOf(City(1, "Москва", 55.755888, 37.617333),
            City(2, "Волгоград",48.7194, 44.5018),
            City(3, "Волжский", 48.785888, 44.779777))
    }

    override fun getCities() = _cities!!

    private val _city : City by lazy { getCities()[2] }

    override fun getCurrentCity() = _city

    /*override fun getWeatherForecasts(city: City): MutableList<DayTimeWeatherForecast> {

        val weatherForecast : MutableList<DayTimeWeatherForecast>

        weatherForecast = arrayListOf(
            DayTimeWeatherForecast(DaysTime.Morning, Weather(WeatherEvent.Wind(speed = 3.2), 2)),
            DayTimeWeatherForecast(DaysTime.Noon, Weather(WeatherEvent.Wind(speed = 5.5), 0)),
            DayTimeWeatherForecast(DaysTime.Evening, Weather(WeatherEvent.Rain(volume = 5.0), 1)),
            DayTimeWeatherForecast(DaysTime.Night, Weather(WeatherEvent.Rain(volume = 10.5), 0))
        )

        return weatherForecast
    }

    override fun getWeekWeatherForecasts(city: City) : MutableList<DayWeatherForecast> {
        val dayWeatherForecasts : MutableList<DayWeatherForecast> = arrayListOf()

        for (weekDay in WeekDay.values()) {
            val dayWeatherForecast = getRandomDayWeatherForecasts(weekDay)
            dayWeatherForecasts.add(dayWeatherForecast)
        }

        return dayWeatherForecasts
    }

    private fun getRandomDayWeatherForecasts(weekDay: WeekDay) : DayWeatherForecast {

        val timeWeatherForecasts : MutableList<DayTimeWeatherForecast> = arrayListOf()
        for (daysTime in DaysTime.values()) {

            val timeWeatherForecast = DayTimeWeatherForecast(daysTime, Weather.randomWeather())
            timeWeatherForecasts.add(timeWeatherForecast)
        }
        return DayWeatherForecast(weekDay, timeWeatherForecasts)
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    public fun getWeatherForecasts(weatherDTO: WeatherDTO) : MutableList<DayWeatherForecast> {
        val weatherForecasts : MutableList<DayWeatherForecast> = arrayListOf()
        if (weatherDTO.forecasts == null)
            return weatherForecasts

        for (forecast in weatherDTO.forecasts!!) {
            val date = LocalDate.parse(forecast.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val day = WeekDay.values()[date.dayOfWeek.ordinal]

            val timeWeatherForecasts : MutableList<DayTimeWeatherForecast> = arrayListOf()
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Morning, Weather.fromDTO(forecast.parts!!.morning)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Noon, Weather.fromDTO(forecast.parts!!.day)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Evening, Weather.fromDTO(forecast.parts!!.evening)))
            timeWeatherForecasts.add(DayTimeWeatherForecast(DaysTime.Night, Weather.fromDTO(forecast.parts!!.night)))

            weatherForecasts.add((DayWeatherForecast(day, timeWeatherForecasts)))
        }
        return weatherForecasts
    }
}