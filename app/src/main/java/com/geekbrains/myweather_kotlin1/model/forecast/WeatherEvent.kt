package com.geekbrains.myweather_kotlin1.model.forecast

sealed class WeatherEvent(val name: String, val eventImg: String?) {

    class Clear(img: String?) : WeatherEvent("Ясно", img) {
        override fun toString() = "$name"
    }

    data class Wind(val speed: Double, val img: String) : WeatherEvent("Ветер", img) {
        override fun toString() = "$name ($speed м/с)"
    }

    data class Rain(val volume: Double, val img: String) : WeatherEvent("Дождь", img) {
        override fun toString(): String = "$name ($volume мм)"
    }

    data class Snow(val volume: Double, val img: String) : WeatherEvent("Снег", img) {
        override fun toString(): String = "$name ($volume мм)"
    }
}
