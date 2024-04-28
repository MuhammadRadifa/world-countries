package com.dicoding.worldcountries.data.network.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    companion object{
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context):FavoriteDatabase{
            if (INSTANCE == null){
                synchronized(FavoriteDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteDatabase::class.java, "favorite_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as FavoriteDatabase
        }
    }
}