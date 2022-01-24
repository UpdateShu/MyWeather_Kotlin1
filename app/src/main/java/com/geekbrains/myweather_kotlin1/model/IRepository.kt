package com.geekbrains.myweather_kotlin1.model

import com.geekbrains.myweather_kotlin1.model.forecast.DayTimeWeatherForecast
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast

interface IRepository {

    fun getCities() : ArrayList<City>
    fun getCurrentCity() : City
    fun getWeekWeatherForecasts(city: City) : MutableList<DayWeatherForecast>
    fun getWeatherForecasts(city: City) : MutableList<DayTimeWeatherForecast>
}