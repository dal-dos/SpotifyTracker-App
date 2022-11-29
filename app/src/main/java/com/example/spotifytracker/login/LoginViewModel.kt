package com.example.spotifytracker.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import java.lang.IllegalArgumentException

class LoginViewModel(private val repository: SpotifyDataRepository) : ViewModel() {
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

class LoginViewModelFactory (private val repository: SpotifyDataRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
