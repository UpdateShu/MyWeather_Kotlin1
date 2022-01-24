package com.geekbrains.myweather_kotlin1.model.forecast

sealed class WeatherEvent(val name: String) {

    class Clear() : WeatherEvent("Ясно") {
        override fun toString() = "$name"
    }

    data class Wind(val speed: Int) : WeatherEvent("Ветер") {
        override fun toString() = "$name ($speed м/с)"
    }

    data class Rain(val volume: Int) : WeatherEvent("Дождь") {
        override fun toString(): String = "$name ($volume мм)"
    }

    data class Snow(val speed: Int) : WeatherEvent("Снег") {
        override fun toString(): String = "$name ($speed см/ч)"
    }
}
