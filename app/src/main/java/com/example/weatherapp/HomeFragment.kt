package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(R.layout.fragment_home) {

    val apiKey = "c1b8cca3f8e0025563835d33cad2543e"
    var city = "Lodz"

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        getWeather(city)
        handleSearchAction()
        handleRefreshAction()
    }

    private fun handleSearchAction() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    city = query
                    getWeather(city)
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
            getWeather(city)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun makeRequest(city: String) : Response {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        return client.newCall(request).execute()
    }

    private fun getWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = makeRequest(city)
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Failed to get weather data", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    val json = response.body?.string()
                    val jsonObject = JSONObject(json)
                    updateUI(jsonObject)
                }
            }
        }
    }

    private fun updateUI(jsonObject: JSONObject) {
        val sys = jsonObject.getJSONObject("sys")
        val location =
            jsonObject.getString("name") + ", " + sys.getString("country")

        val updatedAt = jsonObject.getLong("dt")
        val updatedAtText = "Updated at: " + SimpleDateFormat(
            "dd/MM/yyyy hh:mm a",
            Locale.getDefault()
        ).format(Date(updatedAt * 1000))

        val main = jsonObject.getJSONObject("main")
        val minTempCelsius = fahrenheitToCelsius(main.getString("temp_min").toDouble())
        val maxTempCelsius = fahrenheitToCelsius(main.getString("temp_max").toDouble())
        val currTempCelsius = fahrenheitToCelsius(main.getString("temp").toDouble())
        val current_temp = currTempCelsius.toString() + "°C"
        val min_temp = "Min: " + minTempCelsius.toString() + "°C"
        val max_temp = "Max: " + maxTempCelsius.toString() + "°C"

        val pressure = main.getString("pressure")
        val wind = jsonObject.getJSONObject("wind").getString("speed") + "m/s"
        val humidity = main.getString("humidity") + "%"
        val longtitude = (round(jsonObject.getJSONObject("coord").getString("lon").toDouble() * 100) / 100).toString()
        val latitude = (round(jsonObject.getJSONObject("coord").getString("lat").toDouble() * 100) / 100).toString()
        val sunrise = sys.getLong("sunrise")
        val sunriseText = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date(sunrise * 1000))

        val sunset = sys.getLong("sunset")
        val sunsetText = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date(sunset * 1000))

        val fh = main.getString("temp")
        val sky_visibility = ((10000.0 - (jsonObject.getJSONObject("clouds").getString("all")).toDouble()) / 100).toString() + "%"

        // set TextView values
        binding.location.text = location
        binding.updatedAt.text = updatedAtText
        binding.currTemp.text = current_temp
        binding.minTemp.text = min_temp
        binding.maxTemp.text = max_temp

        binding.pressure.text = pressure
        binding.wind.text = wind
        binding.humidity.text = humidity
        binding.longtitude.text = longtitude
        binding.sunrise.text = sunriseText
        binding.sunset.text = sunsetText
        binding.fahrenheitTemp.text = fh
        binding.sky.text = sky_visibility
        binding.latitude.text = latitude

        val weatherIconCheck = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")

        downloadWeatherIcon(weatherIconCheck, binding.weatherIcon)

        binding.weatherLayout.visibility = View.VISIBLE
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return round((fahrenheit - 273.15) * 10.0) / 10.0
    }

    private fun downloadWeatherIcon(iconCode: String, imageView: ImageView) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"

        Glide.with(imageView.context)
            .load(iconUrl)
            .into(imageView)
    }
}