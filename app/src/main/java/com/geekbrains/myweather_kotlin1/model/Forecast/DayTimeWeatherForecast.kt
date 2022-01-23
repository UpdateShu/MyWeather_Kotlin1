package com.geekbrains.myweather_kotlin1.model.Forecast

import com.geekbrains.myweather_kotlin1.model.Forecast.DaysTime
import com.geekbrains.myweather_kotlin1.model.Forecast.Weather

data class DayTimeWeatherForecast(val daysTime: DaysTime, val weather: Weather) {
    override fun toString(): String {
        return """${daysTime.timeName}: ${weather.weatherEvent}, ${weather.temp}.temp C
            """.trimIndent()
    }
}

