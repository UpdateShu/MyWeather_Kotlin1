package com.geekbrains.myweather_kotlin1.repository

import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import retrofit2.Callback

interface IWeatherRepository {
    fun getWeatherForecastsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    )
}