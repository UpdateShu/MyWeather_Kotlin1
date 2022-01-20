package com.geekbrains.myweather_kotlin1.viewmodel

import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.Forecast.DayTimeWeatherForecast
import com.geekbrains.myweather_kotlin1.model.Forecast.DayWeatherForecast

sealed class AppState {

    data class Success(val appData: AppData) : AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}