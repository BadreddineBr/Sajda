package com.example.adhanprayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.adhanprayer.R

class CitySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selection)

        // Get the Spinner and Button from the layout
        val citySpinner: Spinner = findViewById(R.id.citySpinner)
        val nextPageButton: Button = findViewById(R.id.nextPageButton)

        // List of cities
        val cities = listOf(
            "Casablanca", "Marrakech", "Tanger", "Rabat", "Fez",
            "Agadir", "Essaouira", "Oujda", "Tangiers", "Meknes",
            "Taza", "Tetouan", "Ksar el-Kébir", "Nador", "El Jadida",
            "Kenitra", "Berkane", "Safi", "Dakhla", "Tiznit",
            "Ifrane", "Al Hoceima", "Taroudant", "Azilal", "Errachidia",
            "Chtouka Ait Baha", "Moulay Yacoub", "Sidi Kacem"
        )

        // Create an ArrayAdapter for the Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // Set an OnClickListener for the button
        nextPageButton.setOnClickListener {
            // Get the selected city
            val selectedCity = citySpinner.selectedItem.toString()

            // Create an Intent to go to PrayerTimesActivity
            val intent = Intent(this, PrayerTimesActivity::class.java)
            intent.putExtra("city", selectedCity)  // Pass the selected city
            startActivity(intent)  // Start the PrayerTimesActivity
        }
    }
}
