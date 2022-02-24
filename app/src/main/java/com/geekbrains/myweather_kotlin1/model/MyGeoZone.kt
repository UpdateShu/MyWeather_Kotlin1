package com.geekbrains.myweather_kotlin1.model

import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class MyGeoZone(val id: Int,
                     val myGeoMarker: MyGeoMarker,
                      val transitionType: Int) : Serializable {

    fun toGeoZone(): Geofence {
        return Geofence.Builder()
            .setRequestId(id.toString())
            .setTransitionTypes(transitionType)
            .setCircularRegion(myGeoMarker.lat, myGeoMarker.lon, myGeoMarker.radius)
            .setExpirationDuration(ONE_MINUTE.toLong())
            .build()
    }

    companion object {
        private const val ONE_MINUTE = 60000
    }
}