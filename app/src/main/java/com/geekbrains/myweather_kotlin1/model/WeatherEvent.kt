package com.geekbrains.myweather_kotlin1.model

sealed class WeatherEvent(val name: String) {

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
}
