package com.geekbrains.myweather_kotlin1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val Id: Int,
                val name: String,
                val cityLat: Double,
                val cityLon: Double,
                var isChecked: Boolean = false) : WeatherLocation(cityLat, cityLon)