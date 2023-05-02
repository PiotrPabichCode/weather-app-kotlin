package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherHomeData
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.Utils.fahrenheitToCelsius
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val mainVM by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        getWeather(mainVM.homeCity)
        handleSearchAction()
        handleRefreshAction()
    }

    override fun onStart() {
        super.onStart()
        Log.d("ON START: ", "1")
        getWeather(mainVM.homeCity)
    }

    private fun handleSearchAction() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    mainVM.homeCity = query
                    getWeather(mainVM.homeCity)
                    binding.searchView.setQuery("", false)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun handleRefreshAction() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getWeather(mainVM.homeCity)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun makeRequest(city: String) : Response {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=${Constants.API_KEY}"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        return client.newCall(request).execute()
    }

    private fun getWeather(city: String) {
        Log.d("GET WEATHER BEFORE: ", city)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("GET WEATHER SCOPE: ", "1")

            val response = makeRequest(city)
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Failed to get weather data", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    val data = Gson().fromJson(response.body?.charStream(), WeatherHomeData::class.java)
                    updateUI(data)
                }
            }
        }
    }

    private fun updateUI(data: WeatherHomeData) {
        val sys = data.sys
        val location = "${data.name}, ${sys.country}"

        val updatedAt = data.dt
        val updatedAtText = "Updated at: " + SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date(updatedAt * 1000))

        val main = data.main
        val minTemp = fahrenheitToCelsius(main.temp_min)
        val maxTemp = fahrenheitToCelsius(main.temp_max)
        val currTemp = fahrenheitToCelsius(main.temp)

        val currTempText = "$currTemp°C"
        val minTempText = "Min: $minTemp°C"
        val maxTempText = "Max: $maxTemp°C"

        val pressure = main.pressure
        val wind = "${data.wind.speed} m/s"
        val humidity = "${main.humidity} %"
        val longitude = (round(data.coord.lon * 100) / 100).toString()
        val latitude = (round(data.coord.lat * 100) / 100).toString()
        val sunrise = sys.sunrise
        val sunriseText = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date(sunrise * 1000))

        val sunset = sys.sunset
        val sunsetText = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date(sunset * 1000))

        val fh = main.temp
        val sky = (10000.0 - data.clouds.all) / 100
        val skyText = "$sky %"

        binding.location.text = location
        binding.updatedAt.text = updatedAtText
        binding.currTemp.text = currTempText
        binding.minTemp.text = minTempText
        binding.maxTemp.text = maxTempText

        binding.pressure.text = pressure.toString()
        binding.wind.text = wind
        binding.humidity.text = humidity
        binding.longtitude.text = longitude
        binding.sunrise.text = sunriseText
        binding.sunset.text = sunsetText
        binding.fahrenheitTemp.text = fh.toString()
        binding.sky.text = skyText
        binding.latitude.text = latitude

        val weatherIcon = data.weather[0].icon
        downloadWeatherIcon(weatherIcon, binding.weatherIcon)
        binding.weatherLayout.visibility = View.VISIBLE
    }

    fun getLocation() : String {
        return binding.location.toString()
    }

    private fun downloadWeatherIcon(iconCode: String, imageView: ImageView) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"

        Glide.with(imageView.context)
            .load(iconUrl)
            .into(imageView)
    }
}