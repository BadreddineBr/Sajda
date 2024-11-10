package com.example.adhanprayer.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adhanprayer.R

class CityAdapter(private val cities: List<String>, private val listener: (String) -> Unit) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city, listener)
    }

    override fun getItemCount(): Int = cities.size

    class CityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cityName: TextView = view.findViewById(R.id.cityName)

        fun bind(city: String, listener: (String) -> Unit) {
            cityName.text = city
            itemView.setOnClickListener { listener(city) }
        }
    }
}
