package com.geekbrains.myweather_kotlin1.model.forecast

data class TimeWeatherDTO (
    val temp_avg: Int,
    val condition: String,
    val prec_mm: Double,
    val wind_speed: Double)