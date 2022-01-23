package com.geekbrains.myweather_kotlin1.model

interface IRepository {

    fun getCurrentCity() : City
    fun getWeatherForecasts(city: City) : MutableList<WeatherForecast>
}