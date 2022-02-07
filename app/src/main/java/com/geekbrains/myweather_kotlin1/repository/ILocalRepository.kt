package com.geekbrains.myweather_kotlin1.repository

import android.content.SharedPreferences
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather
import com.google.android.material.internal.ManufacturerUtils

interface ILocalRepository {
    fun saveCurrentCity(city: City, preferences: SharedPreferences)
    fun getCurrentCity(preferences: SharedPreferences) : City?

    fun saveHistory(city: City, weekWeather: WeekWeather)
    fun getCityHistory(city: City): MutableList<HistoryForecast>
}