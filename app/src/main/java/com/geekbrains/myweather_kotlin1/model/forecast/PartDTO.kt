package com.geekbrains.myweather_kotlin1.model.forecast

data class PartDTO(
    val morning : TimeWeatherDTO,
    val day : TimeWeatherDTO,
    val evening: TimeWeatherDTO,
    val night : TimeWeatherDTO,
)