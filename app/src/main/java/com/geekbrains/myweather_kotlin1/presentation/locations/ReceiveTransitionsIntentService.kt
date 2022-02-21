package com.geekbrains.myweather_kotlin1.presentation.locations

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent

import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class ReceiveTransitionsIntentService : IntentService(TRANSITION_INTENT_SERVICE) {
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
            Log.d("GEO", "onHandle:" + geofence.requestId)
            //processGeofence(geofence, transitionType)
        }
    }

    /*private fun processGeofence(geofence: Geofence, transitionType: Int) {
        val notificationBuilder: NotificationCompat.Builder = Builder(applicationContext)
        val openActivityIntetnt = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                MainActivity::class.java
            ), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val id = geofence.requestId.toInt()
        val transitionTypeString = getTransitionTypeString(transitionType)
        notificationBuilder
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Geofence id: $id")
            .setContentText("Transition type: $transitionTypeString")
            .setVibrate(longArrayOf(500, 500))
            .setContentIntent(openActivityIntetnt)
            .setAutoCancel(true)
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(transitionType * 100 + id, notificationBuilder.build())
        Log.d("GEO", String.format("notification built:%d %s", id, transitionTypeString))
    }

    private fun getTransitionTypeString(transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "enter"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "exit"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "dwell"
            else -> "unknown"
        }
    }*/

    companion object {
        const val TRANSITION_INTENT_SERVICE = "TransitionsService"
    }
}