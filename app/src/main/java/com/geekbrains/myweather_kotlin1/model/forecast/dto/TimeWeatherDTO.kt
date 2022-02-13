package com.geekbrains.myweather_kotlin1.model.forecast.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeWeatherDTO (
    val temp_avg: Int,
    val condition: String,
    val icon : String,
    val prec_mm: Double,
    val wind_speed: Double) : Parcelable