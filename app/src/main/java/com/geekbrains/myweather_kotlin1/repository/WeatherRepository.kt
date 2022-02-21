package com.geekbrains.myweather_kotlin1.repository

import com.geekbrains.myweather_kotlin1.model.ILocation
import com.geekbrains.myweather_kotlin1.model.WeatherLocation
import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import retrofit2.Callback

class WeatherRepository(private val remoteDataSource: RemoteDataSource): IWeatherRepository {
    override fun getWeatherForecastsFromServer(
        location : WeatherLocation,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherForecasts(location, callback)
    }

}