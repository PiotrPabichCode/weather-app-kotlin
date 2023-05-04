package com.example.weatherapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.weatherapp.data.api.*

@Entity(tableName = "weather_home_table")
data class WeatherHomeData(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Long,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)