package com.example.weatherapp.utils

import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.data.entities.FavouriteCity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.round

object Utils {

    suspend fun searchCities(query: String): List<FavouriteCity> = withContext(Dispatchers.IO) {
        val url = "https://api.openweathermap.org/data/2.5/find?q=$query&type=like&appid=${Constants.API_KEY}"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val cities = mutableListOf<FavouriteCity>()
            if (body != null) {
                Gson().fromJson(body, JsonObject::class.java).getAsJsonArray("list")
                    ?.forEach { result ->
                        val name = result.asJsonObject.get("name").asString
                        val main = result.asJsonObject.getAsJsonObject("main")
                        val temp = main.get("temp").asDouble
                        val sys = result.asJsonObject.getAsJsonObject("sys")
                        val country = sys.get("country").asString
                        val city = FavouriteCity(name = name, country = country, temp = temp)
                        cities.add(city)
                    }
            }
            cities
        } catch(e: Exception) {
            throw e
        }
    }

    fun convertKelvin(kelvin: Double, mainVM: MainViewModel): Double {
        if(mainVM.isFahrenheitMode) {
            return round(((kelvin - 273.15) * 1.8 + 32)  * 10.0) / 10.0
        } else {
            return round((kelvin - 273.15) * 10.0) / 10.0
        }
    }

}