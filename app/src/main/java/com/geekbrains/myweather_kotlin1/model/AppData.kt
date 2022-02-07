package com.geekbrains.myweather_kotlin1.model

import com.geekbrains.myweather_kotlin1.model.db.HistoryEntity
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather

data class AppData(
    var cities: List<City>,
    var currentCity: City?,
    var weekWeather: WeekWeather?,
    var historyEntities: MutableList<HistoryForecast>
) {
    constructor() : this(emptyList<City>(), null, null, mutableListOf())
}