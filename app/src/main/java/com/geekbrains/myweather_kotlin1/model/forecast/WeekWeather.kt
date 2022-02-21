package com.geekbrains.myweather_kotlin1.model.forecast

import com.geekbrains.myweather_kotlin1.model.City
import java.time.LocalDate

data class WeekWeather(val date: LocalDate, val forecasts: MutableList<DayWeatherForecast>)