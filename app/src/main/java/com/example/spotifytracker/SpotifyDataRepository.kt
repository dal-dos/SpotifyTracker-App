package com.example.spotifytracker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SpotifyDataRepository(private val spotifyDataDao: SpotifyDataDao) {

    val allEntries: Flow<List<SpotifyDataEntity>> = spotifyDataDao.getAll()

    fun insertInputType(exerciseEntry: SpotifyDataEntity){
        CoroutineScope(Dispatchers.IO).launch{
            spotifyDataDao.insertExcerciseEntry(exerciseEntry)
        }
    }


    fun deleteEntry(key: Long){
        CoroutineScope(Dispatchers.IO).launch {
            spotifyDataDao.deleteEntry(key)
        }
    }

}