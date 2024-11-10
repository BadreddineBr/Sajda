package com.example.adhanprayer.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.adhanprayer.R
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationUtils {

    companion object {
        private const val CHANNEL_ID = "PrayerNotificationChannel"
        private const val CHANNEL_NAME = "Prayer Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for prayer times"
    }

    @SuppressLint("MissingPermission")
    fun showPrayerNotification(context: Context, prayerName: String?, prayerTime: Instant?) {
        // Ensure the Instant value is passed here
        val formattedPrayerTime = if (prayerTime != null) {
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            formatter.format(Date(prayerTime.toEpochMilliseconds()))  // Format Instant to String
        } else {
            "Unknown Time"
        }

        // Create the notification channel if necessary (required for Android 8.0 and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.sajda)
            .setContentTitle("Heure de Salat $prayerName ")
            .setContentText("Il est temps pour la prière de $prayerName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Display the notification
        with(NotificationManagerCompat.from(context)) {
            notify(prayerName.hashCode(), notificationBuilder.build())
        }
    }

}
