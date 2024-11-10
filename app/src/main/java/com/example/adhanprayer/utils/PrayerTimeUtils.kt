package com.example.adhanprayer.utils

import com.example.adhanprayer.data.models.PrayerTimesModel
import java.text.SimpleDateFormat
import java.util.*

object PrayerTimeUtils {

    fun formatPrayerTimes(prayerTimes: PrayerTimesModel): Map<String, String> {
        val formattedTimes = mutableMapOf<String, String>()

        // Create a SimpleDateFormat to format time
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Convert prayer times (assuming they're in milliseconds) to string format
        formattedTimes["Fajr"] = timeFormat.format(Date(prayerTimes.fajr))
        formattedTimes["Dhuhr"] = timeFormat.format(Date(prayerTimes.dhuhr))
        formattedTimes["Asr"] = timeFormat.format(Date(prayerTimes.asr))
        formattedTimes["Maghrib"] = timeFormat.format(Date(prayerTimes.maghrib))
        formattedTimes["Isha"] = timeFormat.format(Date(prayerTimes.isha))

        return formattedTimes
    }
}
