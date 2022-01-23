package com.geekbrains.myweather_kotlin1.model

data class WeatherForecast(val daysTime: DaysTime, val weather: Weather) {
    override fun toString(): String {
        return daysTime.timeName + ": " + weather.weatherEvent + ", " + weather.temp + " C"
    }
}

