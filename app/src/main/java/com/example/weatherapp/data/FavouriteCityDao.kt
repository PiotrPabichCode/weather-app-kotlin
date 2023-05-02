package com.example.weatherapp.data

import androidx.room.*

@Dao
interface FavouriteCityDao {

    @Insert
    suspend fun insertAll(favouriteCities: List<FavouriteCity>)

    @Insert
    suspend fun insert(favouriteCity: FavouriteCity)

    @Delete
    suspend fun deleteALL(favouriteCities: List<FavouriteCity>)

    @Delete
    suspend fun delete(favouriteCity: FavouriteCity)

    @Update
    suspend  fun update(favouriteCity: FavouriteCity)

    @Query("SELECT * FROM favourite_table")
    fun getAll(): List<FavouriteCity>

    @Query("DELETE FROM favourite_table")
    suspend fun dropDatabase()
}