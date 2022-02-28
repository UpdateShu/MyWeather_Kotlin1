package com.geekbrains.myweather_kotlin1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface ILocation {
    val lat : Double
    val lon: Double
}