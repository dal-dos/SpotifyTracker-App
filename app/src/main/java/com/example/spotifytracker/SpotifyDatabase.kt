package com.example.spotifytracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SpotifyDataEntity::class], version = 1)
abstract class SpotifyDatabase: RoomDatabase() {
    abstract val  spotifyDataDao: SpotifyDataDao
    companion object{
        @Volatile
        private var INSTANCE: SpotifyDatabase? = null
        private const val databaseName : String = "SpotifyDataDB"
        fun getInstance(context: Context) : SpotifyDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, SpotifyDatabase::class.java, databaseName).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}