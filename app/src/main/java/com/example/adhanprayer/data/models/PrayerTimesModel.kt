package com.example.adhanprayer.data.models

data class PrayerTimesModel(
    val fajr: Long,  // time in milliseconds
    val dhuhr: Long,
    val asr: Long,
    val maghrib: Long,
    val isha: Long
)
