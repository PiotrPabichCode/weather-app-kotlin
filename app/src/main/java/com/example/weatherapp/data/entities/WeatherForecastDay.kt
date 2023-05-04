package com.example.weatherapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast_table")
data class WeatherForecastDay(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day: String,
    val rain: Double,
    val weatherIcon: String,
    val minTemp: Double,
    val maxTemp: Double
)
