package com.example.adhanprayer.data.repositories

import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.PrayerTimes
import com.example.adhanprayer.utils.PrayerTimeUtils
import com.example.adhanprayer.data.models.PrayerTimesModel

import java.util.*

class PrayerTimesRepository {

    interface Callback {
        fun onSuccess(prayerTimes: Map<String, String>)
        fun onError(errorMessage: String)
    }

    fun fetchPrayerTimes(city: String, callback: Callback) {
        try {
            // Simulate fetching coordinates (you can replace this with an actual geocoding API)
            val coordinates = when(city) {
                "Casablanca" -> Coordinates(33.5731, -7.5898)  // Example coordinates for Casablanca
                "Marrakech" -> Coordinates(31.6349, -7.9994)  // Example coordinates for Marrakech
                "Tanger" -> Coordinates(35.7595, -5.8330)     // Example coordinates for Tanger
                "Fez" -> Coordinates(34.0330, -5.0000)        // Example coordinates for Fez
                else -> throw Exception("City not found")
            }

            // Get current date
            val calendar = Calendar.getInstance()
            val dateComponents = com.batoulapps.adhan2.data.DateComponents(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

            // Calculate prayer times using the Muslim World League method
            val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
            val prayerTimes = PrayerTimes(coordinates, dateComponents, params)

            val formattedPrayerTimes = PrayerTimeUtils.formatPrayerTimes(
                PrayerTimesModel(
                    fajr = prayerTimes.fajr.toEpochMilliseconds(),
                    dhuhr = prayerTimes.dhuhr.toEpochMilliseconds(),
                    asr = prayerTimes.asr.toEpochMilliseconds(),
                    maghrib = prayerTimes.maghrib.toEpochMilliseconds(),
                    isha = prayerTimes.isha.toEpochMilliseconds()
                )
            )

            callback.onSuccess(formattedPrayerTimes)
        } catch (e: Exception) {
            callback.onError("Error fetching prayer times: ${e.message}")
        }
    }
}
