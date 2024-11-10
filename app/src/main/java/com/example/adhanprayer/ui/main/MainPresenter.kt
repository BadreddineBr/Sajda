package com.example.adhanprayer.ui.main

import com.example.adhanprayer.data.repositories.PrayerTimesRepository

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private val repository = PrayerTimesRepository()

    override fun getPrayerTimesForCity(city: String) {
        view.showLoading()

        // Fetch prayer times for the selected city
        repository.fetchPrayerTimes(city, object : PrayerTimesRepository.Callback {
            override fun onSuccess(prayerTimes: Map<String, String>) {
                view.showPrayerTimes(prayerTimes)
            }

            override fun onError(errorMessage: String) {
                view.showError(errorMessage)
            }
        })

        view.hideLoading()
    }

    override fun getCityList() {
        // Simulate fetching a list of cities (can be extended with an API)
        val cities = listOf("Casablanca", "Marrakech", "Tanger", "Fez")
        view.showCityList(cities)
    }
}
