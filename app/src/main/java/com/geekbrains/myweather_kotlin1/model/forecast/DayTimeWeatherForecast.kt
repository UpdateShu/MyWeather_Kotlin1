package com.geekbrains.myweather_kotlin1.model.forecast

data class DayTimeWeatherForecast(val daysTime: DaysTime, val weather: Weather) {
    override fun toString(): String = """${daysTime.timeName}: ${weather.weatherEvent}, ${weather.temp}.temp C""".trimIndent()
}

