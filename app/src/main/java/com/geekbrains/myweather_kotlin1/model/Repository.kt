package com.geekbrains.myweather_kotlin1.model

import com.geekbrains.myweather_kotlin1.model.forecast.*

object Repository : IRepository {

    private val _cities : ArrayList<City> by lazy {
        arrayListOf(City(1, "Москва", 55.7558, 37.6173),
            City(2, "Волгоград",48.7194, 44.5018),
            City(3, "Волжский", 48.7858, 44.7797))
    }

    override fun getCities() = _cities!!

    private val _city : City by lazy { getCities()[2] }

    override fun getCurrentCity() = _city

    override fun getWeatherForecasts(city: City): MutableList<DayTimeWeatherForecast> {

        val weatherForecast : MutableList<DayTimeWeatherForecast>

        weatherForecast = arrayListOf(
            DayTimeWeatherForecast(DaysTime.Morning, Weather(WeatherEvent.Wind(speed = 3), 2)),
            DayTimeWeatherForecast(DaysTime.Noon, Weather(WeatherEvent.Wind(speed = 5), 0)),
            DayTimeWeatherForecast(DaysTime.Evening, Weather(WeatherEvent.Rain(volume = 5), 1)),
            DayTimeWeatherForecast(DaysTime.Night, Weather(WeatherEvent.Rain(volume = 10), 0))
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
    }
}