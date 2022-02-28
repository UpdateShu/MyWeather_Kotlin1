package com.geekbrains.myweather_kotlin1.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.presentation.locations.LocationMessageService
import com.google.android.gms.location.Geofence

fun Context.showNotification(
    title: String,
    message: String,
    idNotification: Int,
    idChannel: String
) {
    val notificationBuilder =
        NotificationCompat.Builder(this, idChannel).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            setContentTitle(title)
            setContentText(message)
            setVibrate(longArrayOf(500, 500))
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(notificationManager, idChannel)
    }
    notificationManager.notify(idNotification, notificationBuilder.build())
}

private fun getTransitionTypeString(transitionType: Int): String {
    return when (transitionType) {
        Geofence.GEOFENCE_TRANSITION_ENTER -> "enter"
        Geofence.GEOFENCE_TRANSITION_EXIT -> "exit"
        Geofence.GEOFENCE_TRANSITION_DWELL -> "dwell"
        else -> "unknown"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(notificationManager: NotificationManager, idChannel: String) {
    val name = "Channel name"
    val descriptionText = "Channel description"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(idChannel, name, importance).apply {
        description = descriptionText
    }
    notificationManager.createNotificationChannel(channel)
}