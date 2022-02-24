package com.geekbrains.myweather_kotlin1.presentation.locations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.utils.showNotification
import com.google.android.gms.location.Geofence
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LocationMessageService : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 13
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteData = remoteMessage.data
        if (remoteData.isNotEmpty()){
            handleMessage(remoteData.toMap())
        }
    }

    private fun handleMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            applicationContext.showNotification(title, message, NOTIFICATION_ID, CHANNEL_ID)
        }
    }

    override fun onNewToken(p0: String) {
        // ignore
    }
}