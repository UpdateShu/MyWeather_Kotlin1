package com.geekbrains.myweather_kotlin1.model.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO (
    val fact: FactDTO?,
    val forecasts: List<ForecastDTO>?
) : Parcelable
