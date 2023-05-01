package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentWeatherForecastBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class WeatherForecastFragment : Fragment(R.layout.fragment_weather_forecast) {

    private lateinit var binding: FragmentWeatherForecastBinding

    val apiKey = "c1b8cca3f8e0025563835d33cad2543e"
    var city = "Lodz"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherForecastBinding.bind(view)

        getWeather(city)
    }

    private fun makeRequest(city: String) : Response {
        val url = "https://pro.openweathermap.org/data/2.5/forecast/climate?q=$city&appid=$apiKey"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        return client.newCall(request).execute()
    }

    private fun getWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = makeRequest(city)
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Failed to get weather forecast", Toast.LENGTH_SHORT).show()
                }
            } else {
                val json = response.body?.string()
                val jsonObject = JSONObject(json)
                withContext(Dispatchers.Main) {
                    if(isAdded) {
                        val adapter = WeatherForecastAdapter(createDays(jsonObject, requireContext()))
                        binding.weatherForecastRV.layoutManager = LinearLayoutManager(requireContext())
                        binding.weatherForecastRV.adapter = adapter
                    }

                }
            }
        }
    }

    private fun createDays(jsonObject: JSONObject, context: Context): List<WeatherForecastDay> = buildList {
        val jsonArray = jsonObject.getJSONArray("list")
        for(i in 0 until jsonArray.length()) {
            val dayObject = jsonArray.getJSONObject(i)
            val dayName = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date(dayObject.getLong("dt") * 1000))
            val minTemp = fahrenheitToCelsius(dayObject.getJSONObject("temp").getDouble("min"))
            val maxTemp = fahrenheitToCelsius(dayObject.getJSONObject("temp").getDouble("max"))
            val weatherArray = dayObject.getJSONArray("weather")
            val icon = weatherArray.getJSONObject(0).getString("icon")
            val rain = dayObject.optDouble("rain", 0.0)

            val imageURL = "https://openweathermap.org/img/wn/$icon.png"
            val newDay = WeatherForecastDay(dayName, rain, imageURL,minTemp,maxTemp)
            add(newDay)
        }
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return round((fahrenheit - 273.15) * 10.0) / 10.0
    }

    private fun downloadWeatherIcon(iconCode: String, imageView: ImageView, context: Context) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"

        Glide.with(context)
            .load(iconUrl)
            .into(imageView)
    }
}