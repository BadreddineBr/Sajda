package com.example.adhanprayer.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.adhanprayer.R
import com.example.adhanprayer.utils.NotificationUtils
import com.example.adhanprayer.utils.PrayerTimeUtils
import com.batoulapps.adhan2.*
import com.batoulapps.adhan2.data.DateComponents
import com.example.adhanprayer.data.models.PrayerTimesModel
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.*

class PrayerTimesActivity : AppCompatActivity() {

    private val notificationUtils = NotificationUtils()
    private lateinit var countdownTextView: TextView
    private var countdownTime: Long = 0 // Time in milliseconds for the countdown
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer reference
    private var isAdhanPlaying = false // Track if the adhan is playing
    private lateinit var salatNameText: TextView
    private lateinit var pauseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prayer_times)

        countdownTextView = findViewById(R.id.countdownTextView)
        salatNameText = findViewById(R.id.salatNameText)
        pauseButton = findViewById(R.id.pauseButton)

        // Request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        val selectedCity = intent.getStringExtra("city") ?: "Casablanca"

        // Set coordinates based on the selected city
        val coordinates = when (selectedCity) {
            "Casablanca" -> Coordinates(33.5731, -7.5898)
            "Marrakech" -> Coordinates(31.6349, -8.0029)
            "Tanger" -> Coordinates(35.7722, -5.8026)
            "Rabat" -> Coordinates(34.020882, -6.84165)
            "Fez" -> Coordinates(34.020882, -5.0095)
            "Agadir" -> Coordinates(30.4278, -9.5982)  // Add Agadir
            "Essaouira" -> Coordinates(31.5061, -9.7592) // Add Essaouira
            "Oujda" -> Coordinates(34.6817, -1.9109)    // Add Oujda
            "Tangiers" -> Coordinates(35.7734, -5.8019)  // Add Tangiers (alternative spelling)
            "Meknes" -> Coordinates(33.8938, -5.5511)    // Add Meknes
            "Taza" -> Coordinates(34.2333, -4.0167)      // Add Taza
            "Tetouan" -> Coordinates(35.5754, -5.3623)   // Add Tetouan
            "Ksar el-Kébir" -> Coordinates(35.2021, -5.9128) // Add Ksar el-Kébir
            "Nador" -> Coordinates(35.1719, -2.9333)     // Add Nador
            "El Jadida" -> Coordinates(33.2540, -8.5029)  // Add El Jadida
            "Kenitra" -> Coordinates(34.2622, -6.5770)    // Add Kenitra
            "Berkane" -> Coordinates(35.0844, -2.3365)    // Add Berkane
            "Safi" -> Coordinates(32.3000, -9.2400)       // Add Safi
            "Dakhla" -> Coordinates(23.6972, -15.9447)    // Add Dakhla
            "Tiznit" -> Coordinates(29.6849, -9.7378)     // Add Tiznit
            "Ifrane" -> Coordinates(33.5333, -5.0147)     // Add Ifrane
            "Al Hoceima" -> Coordinates(35.2486, -3.9484)  // Add Al Hoceima
            "Taroudant" -> Coordinates(30.4714, -8.8737)   // Add Taroudant
            "Azilal" -> Coordinates(31.6400, -6.6567)     // Add Azilal
            "Errachidia" -> Coordinates(31.9297, -4.4195)  // Add Errachidia
            "Chtouka Ait Baha" -> Coordinates(30.2897, -9.3975) // Add Chtouka Ait Baha
            "Moulay Yacoub" -> Coordinates(33.6356, -5.4789) // Add Moulay Yacoub
            "Sidi Kacem" -> Coordinates(34.3000, -5.2833)  // Add Sidi Kacem
            "Marrakech" -> Coordinates(31.6349, -8.0029)   // Add Marrakech
            else -> Coordinates(33.5731, -7.5898)          // Default to Casablanca
        }


        // Get today's date
        val calendar = Calendar.getInstance()
        val dateComponents = DateComponents(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1, // Months are zero-based in Calendar
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Calculate prayer times for the selected city
        val prayerTimes = PrayerTimes(coordinates, dateComponents, CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters)

        // Convert prayer times into a format
        val formattedPrayerTimes = PrayerTimeUtils.formatPrayerTimes(
            PrayerTimesModel(
                fajr = prayerTimes.fajr.toEpochMilliseconds(),
                dhuhr = prayerTimes.dhuhr.toEpochMilliseconds(),
                asr = prayerTimes.asr.toEpochMilliseconds(),
                maghrib = prayerTimes.maghrib.toEpochMilliseconds(),
                isha = prayerTimes.isha.toEpochMilliseconds()
            )
        )

        // Display the prayer times on the screen
        findViewById<TextView>(R.id.cityNameText).text = selectedCity
        findViewById<TextView>(R.id.fajrTime).text = "  Fajr:                                                           ${formattedPrayerTimes["Fajr"]}"
        findViewById<TextView>(R.id.dhuhrTime).text = "  Dhuhr:                                                        ${formattedPrayerTimes["Dhuhr"]}"
        findViewById<TextView>(R.id.asrTime).text = "  Asr:                                                             ${formattedPrayerTimes["Asr"]}"
        findViewById<TextView>(R.id.maghribTime).text = "  Maghrib:                                                  ${formattedPrayerTimes["Maghrib"]}"
        findViewById<TextView>(R.id.ishaTime).text = "  Isha:                                                           ${formattedPrayerTimes["Isha"]}"

        // Start the countdown for the next prayer
        startCountdown(prayerTimes)

        // Pause/Resume button functionality
        pauseButton.setOnClickListener {
            if (isAdhanPlaying) {
                pauseAdhanSound()
            } else {
                resumeAdhanSound()
            }
        }
    }

    private fun startCountdown(prayerTimes: PrayerTimes) {
        val currentTime = Instant.fromEpochMilliseconds(System.currentTimeMillis()) // Use Instant

        // Find the next prayer time and prayer name
        val (nextPrayerTime, nextPrayerName) = getNextPrayerTime(prayerTimes, currentTime)

        if (nextPrayerTime != null) {
            countdownTime = nextPrayerTime.toEpochMilliseconds() - System.currentTimeMillis()

            // Start the countdown timer
            val countdownTimer = object : CountDownTimer(countdownTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Calculate hours, minutes, and seconds from millisUntilFinished
                    val hours = (millisUntilFinished / 3600000).toInt() // 1 hour = 3600000 milliseconds
                    val minutes = (millisUntilFinished % 3600000 / 60000).toInt() // 1 minute = 60000 milliseconds
                    val seconds = (millisUntilFinished % 60000 / 1000).toInt() // 1 second = 1000 milliseconds

                    // Display the countdown in the format HH:mm:ss
                    countdownTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    // Show notification 5 minutes before the prayer
                    if (hours == 0 && minutes == 5 && seconds == 0) {
                        notificationUtils.showPrayerNotification(
                            this@PrayerTimesActivity,
                            nextPrayerName,
                            nextPrayerTime // Pass Instant, not String
                        )
                    }

                    // When the countdown finishes, show adhan and next prayer notification
                    if (hours == 0 && minutes == 0 && seconds == 0) {
                        // Play adhan sound when the countdown reaches 00:00:00
                        playAdhanSound(nextPrayerName)

                        // Show notification for prayer time
                        notificationUtils.showPrayerNotification(
                            this@PrayerTimesActivity,
                            nextPrayerName,
                            nextPrayerTime // Pass Instant
                        )
                    }
                }

                override fun onFinish() {
                    countdownTextView.text = "00:00:00"
                }
            }

            countdownTimer.start()
        }
    }

    private fun playAdhanSound(prayerName: String?) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.adhan)  // Ensure you have adhan.mp3 in the raw folder
        }
        mediaPlayer?.start()
        isAdhanPlaying = true

        // Show salat name and the pause button
        salatNameText.text = prayerName
        salatNameText.visibility = View.VISIBLE
        pauseButton.visibility = View.VISIBLE

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null // Nullify media player after use
            isAdhanPlaying = false
        }
    }

    // Pause the adhan sound
    private fun pauseAdhanSound() {
        mediaPlayer?.pause()
        isAdhanPlaying = false
    }

    // Resume the adhan sound
    private fun resumeAdhanSound() {
        mediaPlayer?.start()
        isAdhanPlaying = true
    }

    // Find the next prayer time and name based on the current time
    private fun getNextPrayerTime(prayerTimes: PrayerTimes, currentTime: Instant): Pair<Instant?, String?> {
        val prayerTimesList = listOf(
            prayerTimes.fajr to "Fajr",
            prayerTimes.dhuhr to "Dhuhr",
            prayerTimes.asr to "Asr",
            prayerTimes.maghrib to "Maghrib",
            prayerTimes.isha to "Isha"
        )

        // Find the next prayer time
        return prayerTimesList.firstOrNull { it.first > currentTime } ?: Pair(null, null)
    }
}
