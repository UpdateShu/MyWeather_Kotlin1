package com.geekbrains.myweather_kotlin1.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class MyGeoMarker(val name: String,
                       val lat: Double,
                       val lon: Double,
                       val radius: Float) : Serializable {
}