package com.example.spotifytracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyDataDao {
    @Insert
    fun insert(exerciseDataEntry: SpotifyDataEntity)

    @Query("SELECT * FROM SpotifyDataTable")
    fun getAll() : Flow<List<SpotifyDataEntity>>

    @Query("DELETE FROM SpotifyDataTable WHERE id = :key")
    fun deleteEntry(key: Long)

    @Query("DELETE FROM SpotifyDataTable")
    suspend fun deleteAll()
}





