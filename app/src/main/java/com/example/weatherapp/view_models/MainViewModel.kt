package com.example.weatherapp.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.weatherapp.data.entities.FavouriteCity
import com.example.weatherapp.data.entities.WeatherForecastDay
import com.example.weatherapp.data.entities.WeatherHomeData
import com.example.weatherapp.repositories.FavouriteCitiesRepository
import com.example.weatherapp.repositories.WeatherForecastRepository
import com.example.weatherapp.repositories.WeatherHomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FavouriteCitiesRepository(app.applicationContext)
    private val repo1 = WeatherForecastRepository(app.applicationContext)
    private val repo2 = WeatherHomeRepository(app.applicationContext)

    var homeCity: String = ""
    var intervalTime: Int = 0
    var isFahrenheitMode: Boolean = false
    var updateHomeData : Boolean = false
    var isProgressBar : Boolean = true
    var favouriteCities : ArrayList<FavouriteCity> = arrayListOf()
    var kelvinTemp: Double = 0.0

    fun updateFavouriteCities() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                favouriteCities = ArrayList(repo.getAll())
            }
        }
    }

    suspend fun saveHomeWeatherData(weatherHomeData: WeatherHomeData) = withContext(Dispatchers.IO) {
        repo2.dropDatabase()
        repo2.insert(weatherHomeData)
    }

    suspend fun getHomeWeatherData() : WeatherHomeData = withContext(Dispatchers.IO) {
        return@withContext repo2.getData()
    }

    suspend fun getWeatherForecastPrediction() : List<WeatherForecastDay> = withContext(Dispatchers.IO){
        return@withContext repo1.getAll()
    }

    fun saveWeatherForecastPrediction(weatherForecastDays: List<WeatherForecastDay>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                repo1.dropDatabase() // delete old records
                repo1.insertAll(weatherForecastDays)
            }
        }
    }

    fun setHomeCity(city: FavouriteCity) {
        homeCity = city.name
        Log.d("HOME CITY: ", "new home City")
    }

    fun getCities() : List<FavouriteCity> {
        return repo.getAll()
    }

    fun deleteCity(city: FavouriteCity) {
        GlobalScope.launch {
            repo.delete(city)
            favouriteCities = ArrayList(repo.getAll())
        }
    }

    fun addCity(city: FavouriteCity) {
        Log.d("BEFORE", "1")
        GlobalScope.launch {
            Log.d("AFTER", "1")
            repo.insert(city)
            updateFavouriteCities()
        }
    }
}