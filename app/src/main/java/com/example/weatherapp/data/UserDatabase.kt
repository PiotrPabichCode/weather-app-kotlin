package com.example.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.api.Clouds
import com.example.weatherapp.data.dao.FavouriteCityDao
import com.example.weatherapp.data.dao.WeatherForecastDao
import com.example.weatherapp.data.dao.WeatherHomeDao
import com.example.weatherapp.data.entities.FavouriteCity
import com.example.weatherapp.data.entities.WeatherForecastDay
import com.example.weatherapp.data.entities.WeatherHomeData

@Database(entities = [FavouriteCity::class, WeatherForecastDay::class, WeatherHomeData::class], version = 1)
@TypeConverters(WeatherHomeDataTypeConverters::class)
abstract class UserDatabase : RoomDatabase(){
    abstract fun FavouriteCityDao(): FavouriteCityDao
    abstract fun WeatherForecastDao(): WeatherForecastDao
    abstract fun WeatherHomeDao(): WeatherHomeDao
}

object UserDatabaseDb{
    private var db: UserDatabase? = null

    fun getInstance(context: Context): UserDatabase {
        if(db == null) {
            db = Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                "user-database-db"
            ).build()
        }
        return db!!
    }
}