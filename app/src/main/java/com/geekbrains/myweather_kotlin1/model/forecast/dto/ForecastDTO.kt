package com.geekbrains.myweather_kotlin1.model.forecast.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDTO (
    val date: String,
    val parts: PartDTO?) : Parcelable
