package com.example.weatherapp.data.dao

import androidx.room.*
import com.example.weatherapp.data.entities.WeatherForecastDay

@Dao
interface WeatherForecastDao {
    @Insert
    suspend fun insertAll(weatherForecastDays: List<WeatherForecastDay>)

    @Insert
    suspend fun insert(weatherForecastDay: WeatherForecastDay)

    @Delete
    suspend fun deleteALL(weatherForecastDays: List<WeatherForecastDay>)

    @Delete
    suspend fun delete(weatherForecastDay: WeatherForecastDay)

    @Update
    suspend  fun update(weatherForecastDay: WeatherForecastDay)

    @Query("SELECT * FROM weather_forecast_table")
    fun getAll(): List<WeatherForecastDay>

    @Query("DELETE FROM weather_forecast_table")
    suspend fun dropDatabase()
}