package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.adapters.WeatherForecastAdapter
import com.example.weatherapp.data.WeatherForecastDay
import com.example.weatherapp.databinding.FragmentWeatherForecastBinding
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.Utils.convertKelvin
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

class WeatherForecastFragment : Fragment(R.layout.fragment_weather_forecast) {
    private val mainVM by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentWeatherForecastBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherForecastBinding.bind(view)
        binding.weatherForecastRV.layoutManager = LinearLayoutManager(view.context)
        getWeather(mainVM.homeCity)
    }

    override fun onStart() {
        super.onStart()
        Log.d("MAIN HOME: ", mainVM.homeCity)
        getWeather(mainVM.homeCity)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MAIN HOME: ", mainVM.homeCity)
        getWeather(mainVM.homeCity)
    }

    private fun makeRequest(city: String) : Response {
        val url = "https://pro.openweathermap.org/data/2.5/forecast/climate?q=$city&appid=${Constants.API_KEY}"
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
                        binding.progressBar.visibility = View.VISIBLE
                        binding.weatherForecastRV.visibility = View.GONE
                        val adapter = WeatherForecastAdapter(createDays(jsonObject), mainVM)
                        binding.weatherForecastRV.adapter = adapter
                        binding.progressBar.visibility = View.GONE
                        binding.weatherForecastRV.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun createDays(jsonObject: JSONObject): List<WeatherForecastDay> = buildList {
        val jsonArray = jsonObject.getJSONArray("list")
        for(i in 0 until jsonArray.length()) {
            val dayObject = jsonArray.getJSONObject(i)
            val dayName = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date(dayObject.getLong("dt") * 1000))
            val minTemp = convertKelvin(dayObject.getJSONObject("temp").getDouble("min"), mainVM)
            val maxTemp = convertKelvin(dayObject.getJSONObject("temp").getDouble("max"), mainVM)
            val weatherArray = dayObject.getJSONArray("weather")
            val icon = weatherArray.getJSONObject(0).getString("icon")
            val rain = dayObject.optDouble("rain", 0.0)

            val imageURL = "https://openweathermap.org/img/wn/$icon.png"
            val newDay = WeatherForecastDay(dayName, rain, imageURL,minTemp,maxTemp)
            add(newDay)
        }
    }
}