package com.geekbrains.myweather_kotlin1.model

import android.os.Parcel
import android.os.Parcelable

class Repository : IRepository {

    override fun getCurrentCity() : City = City(1, "Волжский")

    override fun getWeatherForecasts(city: City): MutableList<WeatherForecast> {

        val weatherForecast : MutableList<WeatherForecast>

        weatherForecast = arrayListOf(WeatherForecast(DaysTime.Morning, Weather(WeatherEvent.Wind(speed = 3), 2)),
            WeatherForecast(DaysTime.Noon, Weather(WeatherEvent.Wind(speed = 5), 0)),
            WeatherForecast(DaysTime.Evening, Weather(WeatherEvent.Rain(volume = 5), 1)),
            WeatherForecast(DaysTime.Night, Weather(WeatherEvent.Rain(volume = 10), 0)))

        return weatherForecast
    }
}