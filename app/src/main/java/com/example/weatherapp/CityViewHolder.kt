package com.example.weatherapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cityNameTextView: TextView = itemView.findViewById(R.id.city_name)
    val cityTempTextView: TextView = itemView.findViewById(R.id.city_temp)
}