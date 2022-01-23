package com.geekbrains.myweather_kotlin1.viewmodel

import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.WeatherForecast

sealed class AppState {
    data class Success(val weatherForecasts: MutableList<WeatherForecast>, val city: City) : AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}