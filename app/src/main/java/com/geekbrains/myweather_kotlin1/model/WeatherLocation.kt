package com.geekbrains.myweather_kotlin1.model

import android.os.Parcel
import android.os.Parcelable
import com.geekbrains.myweather_kotlin1.model.ILocation
import kotlinx.android.parcel.Parcelize

@Parcelize
open class WeatherLocation(val lat: Double,
                           val lon: Double): Parcelable