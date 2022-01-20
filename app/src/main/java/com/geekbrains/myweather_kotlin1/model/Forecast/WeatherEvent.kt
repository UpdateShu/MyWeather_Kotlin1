package com.geekbrains.myweather_kotlin1.model.Forecast

import com.geekbrains.myweather_kotlin1.model.Repository

sealed class WeatherEvent(val name: String) {

    class Clear() : WeatherEvent("Ясно") {
        override fun toString(): String {
            return "$name"
        }
    }
    data class Wind(val speed: Int) : WeatherEvent("Ветер") {
        override fun toString(): String {
            return "$name ($speed м/с)"
        }
    }
    data class Rain(val volume: Int) : WeatherEvent("Дождь") {
        override fun toString(): String {
            return "$name ($volume мм)"
        }
    }
    data class Snow(val speed: Int) : WeatherEvent("Снег") {
        override fun toString(): String {
            return "$name ($speed см/ч)"
        }
    }
}
