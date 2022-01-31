package com.geekbrains.myweather_kotlin1.model

import androidx.lifecycle.MutableLiveData
import com.geekbrains.myweather_kotlin1.model.forecast.DayTimeWeatherForecast
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.viewmodel.AppState

interface IRepository {

    fun getCities() : ArrayList<City>
    fun getCurrentCity() : City
}