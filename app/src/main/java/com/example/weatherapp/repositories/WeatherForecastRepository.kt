package com.example.weatherapp.repositories

import android.content.Context
import com.example.weatherapp.data.UserDatabaseDb
import com.example.weatherapp.data.dao.WeatherForecastDao
import com.example.weatherapp.data.entities.WeatherForecastDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherForecastRepository(context: Context) : WeatherForecastDao {
    private val dao = UserDatabaseDb.getInstance(context).WeatherForecastDao()
    override suspend fun insertAll(weatherForecastDays: List<WeatherForecastDay>) = withContext(Dispatchers.IO) {
        dao.insertAll(weatherForecastDays)
    }

    override suspend fun insert(weatherForecastDay: WeatherForecastDay) = withContext(Dispatchers.IO) {
        dao.insert(weatherForecastDay)
    }

    override suspend fun deleteALL(weatherForecastDays: List<WeatherForecastDay>) = withContext(Dispatchers.IO) {
        dao.deleteALL(weatherForecastDays)
    }

    override suspend fun delete(weatherForecastDay: WeatherForecastDay) = withContext(Dispatchers.IO) {
        dao.delete(weatherForecastDay)
    }

    override suspend fun update(weatherForecastDay: WeatherForecastDay) = withContext(Dispatchers.IO) {
        dao.update(weatherForecastDay)
    }

    override fun getAll(): List<WeatherForecastDay> {
        return dao.getAll()
    }

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        dao.dropDatabase()
    }

}