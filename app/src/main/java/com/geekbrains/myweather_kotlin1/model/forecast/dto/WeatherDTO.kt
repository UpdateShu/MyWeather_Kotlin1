package com.geekbrains.myweather_kotlin1.model.forecast.dto

import android.os.Parcelable
import com.geekbrains.myweather_kotlin1.model.forecast.dto.FactDTO
import com.geekbrains.myweather_kotlin1.model.forecast.dto.ForecastDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO (
    val fact: FactDTO?,
    val forecasts: List<ForecastDTO>?
) : Parcelable
