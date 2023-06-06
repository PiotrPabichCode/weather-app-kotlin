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
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.activities.dataStore
import com.example.weatherapp.data.entities.WeatherForecastDay
import com.example.weatherapp.databinding.WeatherForecastItemBinding
import com.example.weatherapp.utils.Utils.convertKelvin

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
        val rain = weatherForecastDays[position].rain
        holder.rainTV.text = "$rain%"
        downloadWeatherIcon(weatherForecastDays[position].weatherIcon, holder.weatherIV)
        val tempUnit = if (mainVM.isFahrenheitMode) "°F" else "°C"
        val minTemp = convertKelvin(weatherForecastDays[position].minTemp, mainVM)
        val maxTemp = convertKelvin(weatherForecastDays[position].maxTemp, mainVM)
        holder.minTempTV.text = "$minTemp$tempUnit"
        holder.maxTempTV.text = "$maxTemp$tempUnit"
    }


    private fun downloadWeatherIcon(iconUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(iconUrl)
            .into(imageView)
    }
}