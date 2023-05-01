package com.example.weatherapp

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.round

class Utils {

    suspend fun searchCities(query: String): ArrayList<City> = withContext(Dispatchers.IO) {
        val apiKey = "c1b8cca3f8e0025563835d33cad2543e"
        val url = "https://api.openweathermap.org/data/2.5/find?q=$query&type=like&appid=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val gson = Gson()
        val cities = ArrayList<City>()
        val results = gson.fromJson(body, JsonObject::class.java).getAsJsonArray("list")
        if (results != null) {
            results.forEach { result ->
                val name = result.asJsonObject.get("name").asString
                val main = result.asJsonObject.getAsJsonObject("main")
                val temp = fahrenheitToCelsius(main.get("temp").asDouble)
                cities.add(City(name, temp))
            }
        }
        cities
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return round((fahrenheit - 273.15) * 10.0) / 10.0
    }

}