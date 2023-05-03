package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.activities.dataStore
import com.example.weatherapp.data.WeatherForecastDay
import com.example.weatherapp.databinding.WeatherForecastItemBinding

class WeatherForecastAdapter(private val weatherForecastDays: List<WeatherForecastDay>, private var mainVM: MainViewModel):
    RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder>() {

    private lateinit var context: Context
    private lateinit var dataStore: DataStore<Preferences>

    inner class WeatherForecastViewHolder(binding: WeatherForecastItemBinding): ViewHolder(binding.root) {
        val dayTV = binding.dayTV
        val rainTV = binding.rainTV
        val weatherIV = binding.weatherIV
        val minTempTV = binding.minTempTV
        val maxTempTV = binding.maxTempTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        context = parent.context
        dataStore = context.dataStore
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding = WeatherForecastItemBinding.inflate(inflater, parent, false)
        return WeatherForecastViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return weatherForecastDays.size
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        holder.dayTV.text = weatherForecastDays[position].day
        holder.rainTV.text = weatherForecastDays[position].rain.toString() + "%"
        downloadWeatherIcon(weatherForecastDays[position].weatherIcon, holder.weatherIV)
        val tempUnit = if (mainVM.isFahrenheitMode) "°F" else "°C"
        holder.minTempTV.text = weatherForecastDays[position].minTemp.toString() + tempUnit
        holder.maxTempTV.text = weatherForecastDays[position].maxTemp.toString() + tempUnit
    }


    private fun downloadWeatherIcon(iconUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(iconUrl)
            .into(imageView)
    }
}