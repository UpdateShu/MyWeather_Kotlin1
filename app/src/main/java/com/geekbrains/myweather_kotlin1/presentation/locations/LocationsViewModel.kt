package com.geekbrains.myweather_kotlin1.presentation.locations

import android.graphics.Color
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.repository.Repository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException

class LocationsViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val markers: ArrayList<Marker> = arrayListOf()

    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    fun getCities(currentCity: City?) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            Thread.sleep(100)
            liveDataToObserve.postValue(AppState.Success(AppData().also { data ->
                data.currentCity = currentCity
                data.cities = Repository.getCities()
            }))
        }.start()
    }

    fun initSearchByAddress(geoCoder: Geocoder, searchText: String) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            try {
                val addresses = geoCoder.getFromLocationName(searchText, 1)
                if (addresses.size > 0) {
                    liveDataToObserve.postValue(AppState.Success(AppData().also { data ->
                        data.locations?.let {
                            it.add(LatLng(addresses[0].latitude, addresses[0].longitude))
                        }
                    }))
                }
            } catch (e: IOException) {
                liveDataToObserve.postValue(AppState.Error(e))
            }
        }.start()
    }

    fun getAddressAsync(geoCoder: Geocoder, location: LatLng) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                liveDataToObserve.postValue(AppState.Success(AppData().also { data ->
                    addresses.forEach {
                        data.addresses?.let { lines ->
                            lines.add(it.getAddressLine(0))
                        }
                    }
                }))
            } catch (e: IOException) {
                liveDataToObserve.postValue(AppState.Error(e))
            }
        }.start()
    }

    fun drawLine(map: GoogleMap) {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    fun addMarkerToArray(map: GoogleMap, location: LatLng) {
        val marker = setMarker(map, location, markers.size.toString())
        if (marker != null) {
            markers.add(marker)
        }
    }

    fun setMarker(
        map: GoogleMap,
        location: LatLng,
        searchText: String,
    ): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
    }
}