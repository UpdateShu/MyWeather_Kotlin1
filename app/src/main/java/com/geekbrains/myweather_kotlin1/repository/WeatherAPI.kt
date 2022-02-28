package com.geekbrains.myweather_kotlin1.repository

import com.geekbrains.myweather_kotlin1.model.forecast.dto.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v2/informers")//forecast")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}