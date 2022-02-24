package com.geekbrains.myweather_kotlin1.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.PhoneContact
import com.geekbrains.myweather_kotlin1.model.db.HistoryEntity
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather
import com.google.android.material.internal.ManufacturerUtils

interface ILocalRepository {
    fun saveCurrentCity(city: City, preferences: SharedPreferences)
    fun getCurrentCity(preferences: SharedPreferences) : City?

    fun saveHistory(city: City, weekWeather: WeekWeather)
    fun getCityHistory(city: City): MutableList<HistoryForecast>
    fun getCitiesHistory(cities: ArrayList<City>): MutableList<HistoryForecast>

    fun getContacts(contentResolver: ContentResolver) : MutableList<PhoneContact>
}