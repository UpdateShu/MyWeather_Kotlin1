package com.geekbrains.myweather_kotlin1.model.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartDTO(
    val morning : TimeWeatherDTO,
    val day : TimeWeatherDTO,
    val evening: TimeWeatherDTO,
    val night : TimeWeatherDTO,
) : Parcelable