package com.geekbrains.myweather_kotlin1.model.forecast

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val prec_mm: Int?,
    val condition: String?
)
