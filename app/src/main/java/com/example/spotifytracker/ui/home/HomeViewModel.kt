package com.example.spotifytracker.ui.home

import androidx.lifecycle.*
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
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

    private var _username = MutableLiveData<String>()
    var username : MutableLiveData<String>
        get(){
            return _username
        }
        set(value) {
            _username = value
        }

    private var _recentlyPlayed = MutableLiveData<List<PlayHistory>>()
    var recentlyPlayed : MutableLiveData<List<PlayHistory>>
        get(){
            return _recentlyPlayed
        }
        set(value) {
            _recentlyPlayed = value
        }

    private var _favGenre = MutableLiveData<ArrayList<String>>()
    var favGenre : MutableLiveData<ArrayList<String>>
        get(){
            return _favGenre
        }
        set(value) {
            _favGenre = value
        }

    private var _favArtist = MutableLiveData<List<Artist>>()
    var favArtist : MutableLiveData<List<Artist>>
        get(){
            return _favArtist
        }
        set(value) {
            _favArtist = value
        }

    private var _favTrack = MutableLiveData<List<Track>>()
    var favTrack : MutableLiveData<List<Track>>
        get(){
            return _favTrack
        }
        set(value) {
            _favTrack = value
        }

}
class HomeViewModelFactory(private val repository: SpotifyDataRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}