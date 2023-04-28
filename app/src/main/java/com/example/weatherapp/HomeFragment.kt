package com.example.weatherapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey = "c1b8cca3f8e0025563835d33cad2543e"
        val city = "Lodz"
       // getWeatherData(city)
        getWeather(city, apiKey)
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return round((fahrenheit - 273.15) * 10.0) / 10.0
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
                setTextViewValues(jsonObject)
            }
        }
    }

    fun downloadWeatherIcon(iconCode: String, imageView: ImageView) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"

        Glide.with(imageView.context)
            .load(iconUrl)
            .into(imageView)
    }

    private fun setTextViewValues(jsonObject: JSONObject) {
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
        val longtitude = jsonObject.getJSONObject("coord").getString("lon")
        val latitude = jsonObject.getJSONObject("coord").getString("lat")
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

        val locationTextView = requireView().findViewById<TextView>(R.id.location)
        val updatedAtTextView =
            requireView().findViewById<TextView>(R.id.updatedAt)
        val currentTempTextView =
            requireView().findViewById<TextView>(R.id.currTemp)
        val minTempTextView = requireView().findViewById<TextView>(R.id.minTemp)
        val maxTempTextView = requireView().findViewById<TextView>(R.id.maxTemp)
        val weatherIcon = requireView().findViewById<ImageView>(R.id.weatherIcon)
        val pressureTextView = requireView().findViewById<TextView>(R.id.pressure)
        val windTextView = requireView().findViewById<TextView>(R.id.wind)
        val humidityTextView = requireView().findViewById<TextView>(R.id.humidity)
        val sunriseTextView = requireView().findViewById<TextView>(R.id.sunrise)
        val sunsetTextView = requireView().findViewById<TextView>(R.id.sunset)
        val fahrenheit_tempTextView = requireView().findViewById<TextView>(R.id.fahrenheit_temp)
        val skyTextView = requireView().findViewById<TextView>(R.id.sky)
        val longtitudeTextView = requireView().findViewById<TextView>(R.id.longtitude)
        val latitudeTextView = requireView().findViewById<TextView>(R.id.latitude)

        // set TextView values
        locationTextView.text = location
        updatedAtTextView.text = updatedAtText
        currentTempTextView.text = current_temp
        minTempTextView.text = min_temp
        maxTempTextView.text = max_temp

        pressureTextView.text = pressure
        windTextView.text = wind
        humidityTextView.text = humidity
        longtitudeTextView.text = longtitude
        sunriseTextView.text = sunriseText
        sunsetTextView.text = sunsetText
        fahrenheit_tempTextView.text = fh
        skyTextView.text = sky_visibility
        latitudeTextView.text = latitude

        val weatherIconCheck = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")

        downloadWeatherIcon(weatherIconCheck, weatherIcon)
    }
}