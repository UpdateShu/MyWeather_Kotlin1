package com.geekbrains.myweather_kotlin1.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.LocationsFragmentBinding
import com.geekbrains.myweather_kotlin1.model.*
import com.geekbrains.myweather_kotlin1.presentation.locations.CitiesFragmentAdapter
import com.geekbrains.myweather_kotlin1.presentation.locations.LocationsViewModel
import com.geekbrains.myweather_kotlin1.presentation.locations.OnCityItemViewClickListener
import com.geekbrains.myweather_kotlin1.presentation.weather.WeatherFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

import android.content.Intent
import com.geekbrains.myweather_kotlin1.presentation.locations.GeoZoneService
import com.google.android.gms.location.Geofence

const val REQUEST_CODE = 13
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class LocationsFragment : Fragment() {

    companion object {
        const val EXTRA_LOCATION = "location"

        fun newInstance(bundle: Bundle): LocationsFragment {
            val fragment = LocationsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _locationBinding: LocationsFragmentBinding? = null
    private val locationBinding get() = _locationBinding!!

    private var currentLocation: WeatherLocation? = null
    private var currentCity: City? = null

    private lateinit var map: GoogleMap

    private val myHomeMarker = MyGeoMarker("Volzhsky, my home", 48.782676, 44.776534, 100.0f)
    private val myJobMarker = MyGeoMarker("Volzhsky, my job", 48.777786, 44.782911, 100.0f)

    private val viewModel: LocationsViewModel by lazy {
        ViewModelProvider(this).get(LocationsViewModel::class.java)
    }

    private val adapter = CitiesFragmentAdapter(false, R.layout.item_city, object :
        OnCityItemViewClickListener {
        override fun onCityCheckedChanged(checkedCities: ArrayList<City>) {

        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCityItemViewClick(city: City) {
            showWeather(city)
        }

        override fun onLongCityItemViewClick(city: City) {
            locationBinding.citiesView.recycledViewPool.clear()
            //refreshCities()
        }
    })

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        currentLocation = WeatherLocation(currentCity?.lat ?: myHomeMarker.lat,
            currentCity?.lon ?: myHomeMarker.lon)
        val initialPlace = LatLng(myHomeMarker.lat, myHomeMarker.lon)
        googleMap.addMarker(
            MarkerOptions().position(initialPlace).title(myHomeMarker.name)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
            initialPlace,
            15f
        ))
        googleMap.setOnMapLongClickListener { latLng ->
            context?.let {
                viewModel.getAddressAsync(Geocoder(it), latLng)
            }
            viewModel.addMarkerToArray(map, latLng)
            viewModel.drawLine(map)
        }
        addGeoService(myHomeMarker, Geofence.GEOFENCE_TRANSITION_ENTER)
        addGeoService(myHomeMarker, Geofence.GEOFENCE_TRANSITION_EXIT)

        addGeoService(myJobMarker, Geofence.GEOFENCE_TRANSITION_ENTER)
        addGeoService(myJobMarker, Geofence.GEOFENCE_TRANSITION_EXIT)
    }

    private var idZone : Int = 0
    private fun addGeoService(myGeoMarker: MyGeoMarker, transitionType : Int) {

        context?.let {
            val geoZone = MyGeoZone(idZone, myGeoMarker, transitionType)
            val geoZoneService = Intent(context, GeoZoneService::class.java)
            geoZoneService.putExtra(GeoZoneService.EXTRA_ACTION, GeoZoneService.Action.ADD)
            geoZoneService.putExtra(GeoZoneService.EXTRA_GEOZONE, geoZone)

            it.startService(geoZoneService)
            idZone++
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showWeather(location: WeatherLocation) {
        activity?.supportFragmentManager?.let { manager ->
            manager.beginTransaction()
                .replace(R.id.container, WeatherFragment.newInstance(Bundle().also { bundle ->
                    bundle.putParcelable(EXTRA_LOCATION, location as Parcelable)
                }))
                .addToBackStack("")
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _locationBinding = LocationsFragmentBinding.inflate(inflater, container, false)
        return locationBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationBinding.citiesView.adapter = adapter
        locationBinding.myGeo.setOnClickListener { checkPermission() }

        val location = arguments?.getParcelable<WeatherLocation>(WeatherFragment.EXTRA_LOCATION)
        if (location is City) {
            currentCity = location
        } else {
            currentLocation = location
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        viewModel.getLiveData().observe(viewLifecycleOwner, { data -> setData(data) })
        refreshData()
        locationBinding.searchLocation.buttonSearch.setOnClickListener {
            context?.let {
                viewModel.initSearchByAddress(Geocoder(it), locationBinding.searchLocation.searchAddress.text.toString() )
            }
        }
        locationBinding.searchLocation.buttonWeather.setOnClickListener {
            currentLocation?.let {
                showWeather(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Получить менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private val onLocationListener = object : LocationListener {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAddressAsync(
        context: Context,
        location: Location,
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                locationBinding.myGeo.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    showWeather(WeatherLocation(location.latitude, location.longitude))
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_message))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    private fun refreshData() {
        currentCity?.let {
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }
        viewModel.getCities(currentCity)
    }

    private fun setData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                locationBinding.locationsLoading.visibility = View.GONE
                locationBinding.citiesView.isVisible = true

                if (adapter.itemCount == 0) {
                    adapter.setCities(state.appData.cities)
                } else {
                    state.appData.addresses?.let {
                        if (it.count() != 0) {
                            locationBinding.searchLocation.textAddress.text = it[0]
                        }
                    }
                    state.appData.locations?.let {
                        if (it.count() != 0) {
                            val location = it[0]
                            viewModel.setMarker(map, location,
                                locationBinding.searchLocation.textAddress.text.toString())
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    location,
                                    15f
                                )
                            )
                            currentLocation = WeatherLocation(location.latitude, location.longitude)
                        }
                    }
                }
                state.appData.currentCity?.let {
                    val cityIndex = state.appData.cities.indexOf(it)
                    locationBinding.citiesView.smoothScrollToPosition(cityIndex)
                }
            }
            is AppState.Loading -> {
                context?.showLoading(R.string.loading_message)
            }
            is AppState.Error -> {
                locationBinding.citiesView.showSnackBar(getString(R.string.loading_error),
                    getString(R.string.repeat_loading), { refreshData() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeListener()
        _locationBinding = null
    }
}