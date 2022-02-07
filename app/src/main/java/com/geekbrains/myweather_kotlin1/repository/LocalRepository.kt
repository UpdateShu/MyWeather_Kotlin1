package com.geekbrains.myweather_kotlin1.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.SyncStateContract
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.db.HistoryDao
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather
import com.geekbrains.myweather_kotlin1.utils.Constants
import com.geekbrains.myweather_kotlin1.utils.convertHistoryEntityToForecast
import com.geekbrains.myweather_kotlin1.utils.convertTodayForecastToEntities

class LocalRepository(private val localDataSource: HistoryDao) : ILocalRepository {

    override fun saveCurrentCity(city: City, preferences: SharedPreferences) {
        if (preferences == null)
            return
        val editor = preferences.edit()
        editor?.putString(Constants.CURRENT_CITY, city.name)
        editor?.apply()
    }

    override fun getCurrentCity(preferences: SharedPreferences): City? {
        if (preferences == null || Repository.getCities().count() == 0)
            return null
        val city = preferences.getString(Constants.CURRENT_CITY, null).let {
            Repository.getCities().find { city -> city.name.equals(it) }
        }
        return city ?: Repository.getCities()[0]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveHistory(city: City, weekWeather: WeekWeather) {
        convertTodayForecastToEntities(city, weekWeather).forEach({ localDataSource.insert(it) })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCityHistory(city: City): MutableList<HistoryForecast> {
        return convertHistoryEntityToForecast(city, localDataSource.getDataByCity(city.name))
    }
}
