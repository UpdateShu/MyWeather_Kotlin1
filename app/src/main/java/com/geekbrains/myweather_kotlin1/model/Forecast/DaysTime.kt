package com.geekbrains.myweather_kotlin1.model.Forecast

enum class DaysTime(val number: Int, val timeName: String) {
    Morning(1, "Утро"),
    Noon(2, "День"),
    Evening(3, "Вечер"),
    Night(4, "Ночь")
}