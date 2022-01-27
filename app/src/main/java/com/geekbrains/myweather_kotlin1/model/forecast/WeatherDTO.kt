package com.geekbrains.myweather_kotlin1.model.forecast

data class WeatherDTO(
    val fact: FactDTO?,
    val forecasts: List<ForecastDTO>?
)
