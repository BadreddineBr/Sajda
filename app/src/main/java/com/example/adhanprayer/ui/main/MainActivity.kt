package com.example.adhanprayer.ui.main

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhanprayer.R

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var cityAdapter: CityAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewCities)
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter = MainPresenter(this)
        presenter.getCityList() // Load the cities list
    }

    override fun showPrayerTimes(prayerTimes: Map<String, String>) {
        findViewById<TextView>(R.id.fajrTime).text = "Fajr: ${prayerTimes["Fajr"]}"
        findViewById<TextView>(R.id.dhuhrTime).text = "Dhuhr: ${prayerTimes["Dhuhr"]}"
        findViewById<TextView>(R.id.asrTime).text = "Asr: ${prayerTimes["Asr"]}"
        findViewById<TextView>(R.id.maghribTime).text = "Maghrib: ${prayerTimes["Maghrib"]}"
        findViewById<TextView>(R.id.ishaTime).text = "Isha: ${prayerTimes["Isha"]}"
    }


    override fun showCityList(cities: List<String>) {
        cityAdapter = CityAdapter(cities) { city ->
            // When a city is clicked, get the prayer times for that city
            presenter.getPrayerTimesForCity(city)
        }
        recyclerView.adapter = cityAdapter
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        // Show loading indicator
    }

    override fun hideLoading() {
        // Hide loading indicator
    }
}
