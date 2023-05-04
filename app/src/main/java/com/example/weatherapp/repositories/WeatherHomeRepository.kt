package com.example.weatherapp.repositories

import android.content.Context
import com.example.weatherapp.data.UserDatabaseDb
import com.example.weatherapp.data.dao.WeatherHomeDao
import com.example.weatherapp.data.entities.WeatherHomeData

class WeatherHomeRepository(context: Context) : WeatherHomeDao {
    private val dao = UserDatabaseDb.getInstance(context).WeatherHomeDao()
    override suspend fun insert(weatherHomeData: WeatherHomeData) {
        dao.insert(weatherHomeData)
    }

    override fun getData(): WeatherHomeData {
        return dao.getData()
    }

    override suspend fun dropDatabase() {
        dao.dropDatabase()
    }
}