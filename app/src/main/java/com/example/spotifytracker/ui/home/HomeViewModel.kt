package com.example.spotifytracker.ui.home

import androidx.lifecycle.*
import com.example.spotifytracker.SpotifyDataEntity
import com.example.spotifytracker.SpotifyDataRepository
import java.lang.IllegalArgumentException

class HomeViewModel(private val repository: SpotifyDataRepository) : ViewModel() {
    val allLiveData = repository.allEntries.asLiveData()

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

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome, TestUser1635"
    }

    val text: LiveData<String> = _text
}
class HomeViewModelFactory(private val repository: SpotifyDataRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}