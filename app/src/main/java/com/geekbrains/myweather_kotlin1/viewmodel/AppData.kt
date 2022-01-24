package com.geekbrains.myweather_kotlin1.viewmodel

import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast

data class AppData(
    var cities: List<City>,
    var currentCity: City?,
    var weatherForecasts: List<DayWeatherForecast>
) {
    constructor() : this(emptyList<City>(), null, emptyList<DayWeatherForecast>())
}