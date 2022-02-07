package com.geekbrains.myweather_kotlin1.model.forecast

enum class DaysTime(val number: Int, val timeName: String) {
    Morning(1, "Утро"),
    Day(2, "День"),
    Evening(3, "Вечер"),
    Night(4, "Ночь")
}