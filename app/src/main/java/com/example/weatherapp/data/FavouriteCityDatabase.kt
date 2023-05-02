package com.example.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteCity::class], version = 1)
abstract class FavouriteCityDatabase : RoomDatabase(){
    abstract fun FavouriteCityDao(): FavouriteCityDao
}

object FavouriteCityDb{
    private var db: FavouriteCityDatabase? = null

    fun getInstance(context: Context): FavouriteCityDatabase {
        if(db == null) {
            db = Room.databaseBuilder(
                context,
                FavouriteCityDatabase::class.java,
                "favourite-database"
            ).build()
        }
        return db!!
    }
}