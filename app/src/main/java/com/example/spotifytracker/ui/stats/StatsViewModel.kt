package com.example.spotifytracker.ui.stats

import androidx.lifecycle.*
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class StatsViewModel(private val repository: SpotifyDataRepository) : ViewModel() {
    val allStatLiveData = repository.allEntries.asLiveData()

    fun insert(data: SpotifyDataEntity){
        repository.insert(data)
    }

    fun deleteEntry(id: Long){
        val dataList = allStatLiveData.value
        if (dataList != null && dataList.isNotEmpty()){
            repository.deleteEntry(id)
        }
    }

    fun deleteAll(){
        val dataList = allStatLiveData.value
        if (dataList != null && dataList.isNotEmpty()){
            repository.deleteAll()
        }
    }

    private var _usernameStat = MutableLiveData<String>()
    var usernameStat : MutableLiveData<String>
        get(){
            return _usernameStat
        }
        set(value) {
            _usernameStat = value
        }

    private var _recentlyStatPlayed = MutableLiveData<List<PlayHistory>>()
    var recentlyPlayed : MutableLiveData<List<PlayHistory>>
        get(){
            return _recentlyStatPlayed
        }
        set(value) {
            _recentlyStatPlayed = value
        }

    private var _favStatGenre = MutableLiveData<ArrayList<String>>()
    var favGenre : MutableLiveData<ArrayList<String>>
        get(){
            return _favStatGenre
        }
        set(value) {
            _favStatGenre = value
        }

    private var _favStatArtist = MutableLiveData<List<Artist>>()
    var favStatArtist : MutableLiveData<List<Artist>>
        get(){
            return _favStatArtist
        }
        set(value) {
            _favStatArtist = value
        }

    private var _favStatTrack = MutableLiveData<List<Track>>()
    var favStatTrack : MutableLiveData<List<Track>>
        get(){
            return _favStatTrack
        }
        set(value) {
            _favStatTrack = value
        }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


}

class StatsViewModelFactory(private val repository: SpotifyDataRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(StatsViewModel::class.java))
            return StatsViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}