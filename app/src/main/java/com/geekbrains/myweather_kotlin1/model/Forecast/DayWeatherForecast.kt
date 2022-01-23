package com.geekbrains.myweather_kotlin1.model.Forecast

data class DayWeatherForecast(val day: WeekDay, val timeForecasts: MutableList<DayTimeWeatherForecast>)
