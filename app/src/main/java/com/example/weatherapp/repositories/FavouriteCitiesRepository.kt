package com.example.weatherapp.repositories

import android.content.Context
import com.example.weatherapp.data.entities.FavouriteCity
import com.example.weatherapp.data.dao.FavouriteCityDao
import com.example.weatherapp.data.UserDatabaseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteCitiesRepository(context: Context): FavouriteCityDao {
    private val dao = UserDatabaseDb.getInstance(context).FavouriteCityDao()

    override suspend fun insertAll(favouriteCities: List<FavouriteCity>) = withContext(Dispatchers.IO) {
        dao.insertAll(favouriteCities)
    }

    override suspend fun insert(favouriteCity: FavouriteCity) = withContext(Dispatchers.IO) {
        dao.insert(favouriteCity)
    }

    override suspend fun deleteALL(favouriteCities: List<FavouriteCity>) = withContext(Dispatchers.IO) {
        dao.deleteALL(favouriteCities)
    }

    override suspend fun delete(favouriteCity: FavouriteCity) = withContext(Dispatchers.IO) {
        dao.delete(favouriteCity)
    }


    override suspend fun update(favouriteCity: FavouriteCity) = withContext(Dispatchers.IO) {
        dao.update(favouriteCity)
    }

    override fun getAll(): List<FavouriteCity> {
        return dao.getAll()
    }

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        dao.dropDatabase()
    }

}