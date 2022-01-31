package com.geekbrains.myweather_kotlin1.model.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val prec_mm: Int?,
    val condition: String?
) : Parcelable
