package com.example.spotifytracker.ui.home

import androidx.lifecycle.*
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.WeatherObject
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.ui.playlists.SpotifyPlaylist
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

    private var _suggested = MutableLiveData<List<Track>>()
    var suggested : MutableLiveData<List<Track>>
        get(){
            return _suggested
        }
        set(value) {
            _suggested = value
        }

    private var _playedWeekHistory = MutableLiveData<List<PlayHistory>>()
    var playedWeekHistory : MutableLiveData<List<PlayHistory>>
        get(){
            return _playedWeekHistory
        }
        set(value) {
            _playedWeekHistory = value
        }

    private var _timePlayedDay = MutableLiveData<List<PlayHistory>>()
    var timePlayedDay : MutableLiveData<List<PlayHistory>>
        get(){
            return _timePlayedDay
        }
        set(value) {
            _timePlayedDay = value
        }

    private var _allPlaylists = MutableLiveData<List<SpotifyPlaylist>>()
    var allPlaylists : MutableLiveData<List<SpotifyPlaylist>>
        get(){
            return _allPlaylists
        }
        set(value) {
            _allPlaylists = value
        }

    private var _futureWeather = MutableLiveData<ArrayList<WeatherObject>>()
    var futureWeather: MutableLiveData<ArrayList<WeatherObject>>
        get(){
            return _futureWeather
        }
        set(value) {
            _futureWeather = value
        }

    private var _currWeather = MutableLiveData<WeatherObject>()
    var currWeather: MutableLiveData<WeatherObject>
        get(){
            return _currWeather
        }
        set(value) {
            _currWeather = value
        }

    private var _city = MutableLiveData<String>()
    var city: MutableLiveData<String>
        get(){
            return _city
        }
        set(value) {
            _city = value
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