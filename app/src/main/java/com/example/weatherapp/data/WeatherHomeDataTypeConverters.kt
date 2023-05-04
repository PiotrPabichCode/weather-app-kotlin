package com.example.weatherapp.data

import androidx.room.TypeConverter
import com.example.weatherapp.data.api.*
import com.example.weatherapp.data.entities.WeatherHomeData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherHomeDataTypeConverters {

    @TypeConverter
    fun fromWeatherHomeDataToString(data: WeatherHomeData): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun fromStringToWeatherHomeData(value: String): WeatherHomeData {
        val type = object : TypeToken<WeatherHomeData>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromCloudsToString(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun fromStringToClouds(value: String): Clouds {
        val type = object : TypeToken<Clouds>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromCoordToString(coord: Coord): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun fromStringToCoord(value: String): Coord {
        val type = object : TypeToken<Coord>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromMainToString(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun fromStringToMain(value: String): Main {
        val type = object : TypeToken<Main>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromSysToString(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun fromStringToSys(value: String): Sys {
        val type = object : TypeToken<Sys>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromWeatherListToString(list: List<Weather>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToWeatherList(value: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromWindToString(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun fromStringToWind(value: String): Wind {
        val type = object : TypeToken<Wind>() {}.type
        return Gson().fromJson(value, type)
    }
}
