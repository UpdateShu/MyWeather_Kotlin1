package com.geekbrains.myweather_kotlin1.presentation.locations

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent

import android.util.Log
import androidx.core.app.NotificationCompat
import com.geekbrains.myweather_kotlin1.utils.showNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class ReceiveTransitionsIntentService : IntentService(TRANSITION_INTENT_SERVICE) {

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val TRANSITION_INTENT_SERVICE = "TransitionsService"
    }

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent.hasError()) {
            Log.e(
                TRANSITION_INTENT_SERVICE,
                "Location Services error: " + geofencingEvent.errorCode
            )
            return
        }
        val transitionType = geofencingEvent.geofenceTransition
        val triggeredGeofences = geofencingEvent.triggeringGeofences
        for (geofence in triggeredGeofences) {
            val msg = with(geofencingEvent) {
                "${transitionType} ${triggeringLocation.latitude} ${triggeringLocation.longitude}"
            }
            Log.d("GEO", "onHandle: ${geofence.requestId}, ${msg}")

            showGeofence(geofence, transitionType)
        }
    }

    fun showGeofence(geofence: Geofence, transitionType: Int) {
        val id = geofence.requestId.toInt()
        val transitionTypeString = getTransitionTypeString(transitionType)

        applicationContext.showNotification("Geofence id: ${id}",
            "Transition type: $transitionTypeString",
            transitionType * 100 + id,
            CHANNEL_ID
        )
        Log.d("GEO", String.format("notification built:%d %s", id, transitionTypeString))
    }

    private fun getTransitionTypeString(transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "enter"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "exit"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "dwell"
            else -> "unknown"
        }
    }
}