package com.dicoding.worldcountries.repository

import android.app.Application
import com.dicoding.worldcountries.data.network.local.FavoriteDatabase
import com.dicoding.worldcountries.data.network.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(application: Application) {
    private val favoriteDao = FavoriteDatabase.getDatabase(application).favoriteDao()

    fun getAllFavorites():Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    fun getFavoriteByName(name: String):Flow<FavoriteEntity> = favoriteDao.getFavoriteByName(name)

    suspend fun addFavorite(favorite:FavoriteEntity) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun deleteFavoriteByName(favorite: FavoriteEntity) {
        favoriteDao.deleteFavoriteByName(favorite)
    }
}