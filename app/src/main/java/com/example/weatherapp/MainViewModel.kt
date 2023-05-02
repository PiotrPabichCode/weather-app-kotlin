package com.example.weatherapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.FavouriteCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FavouriteCitiesRepository(app.applicationContext)
    var homeCity: String = "Lodz"
    var favouriteCities : ArrayList<FavouriteCity> = arrayListOf()

    fun updateFavouriteCities() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                favouriteCities = ArrayList(repo.getAll())
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