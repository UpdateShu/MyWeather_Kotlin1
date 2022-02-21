package com.geekbrains.myweather_kotlin1.model

import com.geekbrains.myweather_kotlin1.model.db.HistoryEntity
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.DayWeatherForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather
import com.google.android.gms.maps.model.LatLng

data class AppData(
    var cities: List<City>,
    var currentCity: City?,
    var addresses: MutableList<String>?,
    var locations: MutableList<LatLng>?,
    var weekWeather: WeekWeather?,
    var historyEntities: MutableList<HistoryForecast>,
    var contacts: MutableList<PhoneContact>
) {
    constructor() : this(emptyList<City>(), null, mutableListOf(), mutableListOf(),
        null, mutableListOf(), mutableListOf())
}