package com.example.spotifytracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.asLiveData
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import java.lang.IllegalArgumentException

class SpotifyDataViewModel(private val repository: SpotifyDataRepository): ViewModel() {
    private val allLiveData = repository.allEntries.asLiveData()

    fun insert(data: SpotifyDataEntity){
        repository.insert(data)
    }

    fun deleteEntry(id: Long){
        val dataList = allLiveData.value
        if (dataList != null && dataList.isNotEmpty()){
            repository.deleteEntry(id)
        }
    }

    fun deleteAll(){
        val dataList = allLiveData.value
        if (dataList != null && dataList.isNotEmpty()){
            repository.deleteAll()
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SpotifyDataViewModelFactory (private val repository: SpotifyDataRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(SpotifyDataViewModel::class.java))
            return SpotifyDataViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



