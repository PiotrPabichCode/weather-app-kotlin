package com.example.weatherapp.utils

import android.util.Log
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.data.FavouriteCity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.round

object Utils {

    suspend fun searchCities(query: String, mainVM: MainViewModel): List<FavouriteCity> = withContext(Dispatchers.IO) {
        val url = "https://api.openweathermap.org/data/2.5/find?q=$query&type=like&appid=${Constants.API_KEY}"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val cities = mutableListOf<FavouriteCity>()
        if (body != null) {
            val results = Gson().fromJson(body, JsonObject::class.java).getAsJsonArray("list")
            if (results != null) {
                results.forEach { result ->
                    val name = result.asJsonObject.get("name").asString
                    val main = result.asJsonObject.getAsJsonObject("main")
                    val temp = convertKelvin(main.get("temp").asDouble, mainVM)
                    val sys = result.asJsonObject.getAsJsonObject("sys")
                    val country = sys.get("country").asString
                    val city = FavouriteCity(name = name, country = country, temp = temp)
                    cities.add(city)
                }
            }
        }
        cities
    }

    fun convertKelvin(kelvin: Double, mainVM: MainViewModel): Double {
        if(mainVM.isFahrenheitMode) {
            return round(((kelvin - 273.15) * 1.8 + 32)  * 10.0) / 10.0
        } else {
            return round((kelvin - 273.15) * 10.0) / 10.0
        }
    }

}