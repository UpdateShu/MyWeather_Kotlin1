package com.geekbrains.myweather_kotlin1.presentation.locations

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.geekbrains.myweather_kotlin1.model.MyGeoZone
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.io.Serializable


class GeoZoneService : Service(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val EXTRA_ACTION = "action"
        const val EXTRA_GEOZONE = "geozone"
        const val EXTRA_REQUEST_IDS = "requestId"
    }

    enum class Action : Serializable {
        ADD, REMOVE
    }

    private var transitionType: Int = 0
    private var mAction: Action? = null
    private val mGeoZonesToAdd: MutableList<Geofence> = mutableListOf()

    private var mGoogleApiClient: GoogleApiClient? = null

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d("GEO", "Location service started")

        mAction = intent.getSerializableExtra(EXTRA_ACTION) as Action?
        if (mAction === Action.ADD) {
            val newGeoZone = intent.getSerializableExtra(EXTRA_GEOZONE) as MyGeoZone
            transitionType = newGeoZone.transitionType
            mGeoZonesToAdd.add(newGeoZone.toGeoZone())
        }

        mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API)
            .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()
        mGoogleApiClient?.let {
            it.connect()
        }
        return super.onStartCommand(intent, flags, startId)

    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d("GEO", "Location client connected")
        if (mAction == Action.ADD) {
            val builder = GeofencingRequest.Builder()
            Log.d("GEO", "Location client adds geofence")
            builder.setInitialTrigger(
                if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
                    GeofencingRequest.INITIAL_TRIGGER_ENTER
                else GeofencingRequest.INITIAL_TRIGGER_EXIT
            )
            builder.addGeofences(mGeoZonesToAdd)
            val build = builder.build()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val intent = getPendingIntent()
            intent?.let {
                /*LocationServices.GeofencingApi.addGeofences(mGoogleApiClient!!, build, it)
                    .setResultCallback { status ->
                        if (status.isSuccess) {
                            val msg = "Geofences added: " + status.statusMessage
                            Log.e("GEO", msg)
                            Toast.makeText(this@GeoZoneService, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                        this@GeoZoneService.onResult(status)
                    }*/
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("GEO", "onConnectionSuspended i = $p0")
    }

    private fun getPendingIntent(): PendingIntent? {
        val transitionService = Intent(this, ReceiveTransitionsIntentService::class.java)
        return PendingIntent
            .getService(this, 0, transitionService, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e("GEO", "Location client connection failed: " + connectionResult.getErrorCode())
    }

    override fun onDestroy() {
        Log.d("GEO", "Location service destroyed")
        super.onDestroy()
    }

    fun onResult(@NonNull status: Status) {
        Log.d("GEO", "Geofences onResult$status")
        if (status.isSuccess) {
            mGoogleApiClient!!.disconnect()
            stopSelf()
        } else {
            val text = "Error while geofence: " + status.statusMessage
            Log.e("GEO", text)
            Toast.makeText(this@GeoZoneService, text, Toast.LENGTH_SHORT).show()
        }
    }
}