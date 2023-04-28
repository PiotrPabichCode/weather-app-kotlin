package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.net.URL
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class Weather( val main: String, val description: String)

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey = "c1b8cca3f8e0025563835d33cad2543e"
        val city = "New York"
        getWeather(city, apiKey)
    }

    private fun getWeather(city: String, apiKey: String) {
        // launch a coroutine on the IO dispatcher
        lifecycleScope.launch(Dispatchers.IO) {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
            val request = Request.Builder()
                .url(url)
                .build()
            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            val json = response.body?.string()

            // update the UI on the main thread
            withContext(Dispatchers.Main) {
                // Handle the error
                if (!response.isSuccessful) {
                    // Handle the error
                    return@withContext
                }

                // Parse the JSON response
                val jsonObject = JSONObject(json)

                val sys = jsonObject.getJSONObject("sys")
                val location =
                    jsonObject.getString("name") + ", " + sys.getString("country")

                val updatedAt = jsonObject.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat(
                    "dd/MM/yyyy hh:mm a",
                    Locale.getDefault()
                ).format(Date(updatedAt * 1000))

                val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
                val description = weather.getString("description")

                val main = jsonObject.getJSONObject("main")
                val current_temp = main.getString("temp") + "C"
                val min_temp = "Min Temp: " + main.getString("temp_min") + "C"
                val max_temp = "Max Temp: " + main.getString("temp_max") + "C"

                val locationTextView = requireView().findViewById<TextView>(R.id.current_location)
                val updatedAtTextView =
                    requireView().findViewById<TextView>(R.id.updated_at)
                val descriptionTextView =
                    requireView().findViewById<TextView>(R.id.description)
                val currentTempTextView =
                    requireView().findViewById<TextView>(R.id.current_temp)
                val minTempTextView = requireView().findViewById<TextView>(R.id.min_temp)
                val maxTempTextView = requireView().findViewById<TextView>(R.id.max_temp)

                // set TextView values
                locationTextView.text = location
                updatedAtTextView.text = updatedAtText
                descriptionTextView.text = description
                currentTempTextView.text = current_temp
                minTempTextView.text = min_temp
                maxTempTextView.text = max_temp
            }
        }
    }
}