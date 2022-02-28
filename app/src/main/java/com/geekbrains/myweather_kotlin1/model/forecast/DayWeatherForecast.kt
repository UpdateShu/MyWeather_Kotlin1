package com.geekbrains.myweather_kotlin1.model.forecast

data class DayWeatherForecast(val day: WeekDay, val timeForecasts: MutableList<DayTimeWeatherForecast>)
