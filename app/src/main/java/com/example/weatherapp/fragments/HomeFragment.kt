package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.entities.WeatherHomeData
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.Utils.convertKelvin
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val mainVM by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        Log.d("HOME FRAGMENT ON VIEW CREATED: ", "BINDING CREATED")
        getWeather(mainVM.homeCity)
        handleSearchAction()
        handleRefreshAction()

        // Thread
//        val timer = Timer()
//        timer.scheduleAtFixedRate(object: TimerTask() {
//            override fun run() {
//                getWeather(mainVM.homeCity)
//            }
//        }, 0, 20000)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HOME FRAGMENT ON DESTROY: ", "1")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("HOME FRAGMENT ON DESTROY VIEW: ", "1")
    }

//    override fun onResume() {
//        super.onResume()
//        Log.d("ON RESUME: ", "1")
//        getWeather(mainVM.homeCity)
//    }

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
        try {
            val response = client.newCall(request).execute()
            return response
        } catch(e: Exception) {
            throw e
        }
    }

    private fun getWeather(city: String) {
        Log.d("GET WEATHER BEFORE: ", city)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("GET WEATHER SCOPE: ", "1")

            try {
                val response = makeRequest(city)
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to get weather data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.VISIBLE
                        val data = Gson().fromJson(response.body?.charStream(), WeatherHomeData::class.java)
                        mainVM.saveHomeWeatherData(data)
                        updateUI(data)
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Weather data updated", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch(e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    updateUI(mainVM.getHomeWeatherData())
                    Toast.makeText(context, "No internet connection. Try again later", Toast.LENGTH_SHORT).show()
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
        val minTemp = convertKelvin(main.temp_min, mainVM)
        val maxTemp = convertKelvin(main.temp_max, mainVM)
        val currTemp = convertKelvin(main.temp, mainVM)
        Log.d("CURR TEMP: ", currTemp.toString())

        val tempUnit = if (mainVM.isFahrenheitMode) "°F" else "°C"
        val currTempText = "$currTemp$tempUnit"
        val minTempText = "Min: $minTemp$tempUnit"
        val maxTempText = "Max: $maxTemp$tempUnit"

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

        val kTemp = main.temp
        mainVM.kelvinTemp = kTemp
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
        binding.kelvinTemp.text = kTemp.toString()
        binding.sky.text = skyText
        binding.latitude.text = latitude

        val weatherIcon = data.weather[0].icon
        downloadWeatherIcon(weatherIcon, binding.weatherIcon)
    }

    private fun downloadWeatherIcon(iconCode: String, imageView: ImageView) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"

        Glide.with(imageView.context)
            .load(iconUrl)
            .into(imageView)
    }
}