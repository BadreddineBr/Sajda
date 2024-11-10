package com.example.adhanprayer.ui.main

interface MainContract {
    interface View {
        fun showPrayerTimes(prayerTimes: Map<String, String>)
        fun showCityList(cities: List<String>)
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun getPrayerTimesForCity(city: String)
        fun getCityList()
    }
}
