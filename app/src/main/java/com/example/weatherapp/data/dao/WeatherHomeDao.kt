package com.example.weatherapp.data.dao

import androidx.room.*
import com.example.weatherapp.data.entities.WeatherHomeData

@Dao
interface WeatherHomeDao {

    @Insert
    suspend fun insert(weatherHomeData: WeatherHomeData)

    @Query("SELECT * FROM weather_home_table")
    fun getData(): WeatherHomeData

    @Query("DELETE FROM weather_home_table")
    suspend fun dropDatabase()
}