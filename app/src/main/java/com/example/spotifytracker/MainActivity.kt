package com.example.spotifytracker

// Spotify API

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.databinding.ActivityMainBinding
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.coroutines.launch


@Suppress("RedundantExplicitType")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CLIENT_ID = "9609905ad0f54f66b8d574d367aee504"
    private val REDIRECT_URI = "com.example.spotifytracker://callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var apiHandler: SpotifyApiHandler
    private var username = ""
    private var recentlyPlayed : List<PlayHistory> = arrayListOf()
    private var suggested : List<Track> = arrayListOf()
    private var favoriteGenre : ArrayList<String> = arrayListOf()
    private var favoriteTracks : List<Track> = arrayListOf()
    private var favoriteArtist : List<Artist> = arrayListOf()
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            apiBuilder()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickLogout(view: View) {
        AuthorizationClient.clearCookies(this)
        val intent : Intent = Intent(this, LoginActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putString("Temporary", "Key")
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun apiBuilder(){
        val mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)
        val accessToken = mySharedPreferences.getString("token", "")
        val type = mySharedPreferences.getString("type", "")
        val expires = mySharedPreferences.getInt("expires", 9999)
        val token: Token = Token(accessToken!!, type!!, expires)
        try {
            apiHandler = SpotifyApiHandler(token)
            lifecycleScope.launch() {
                apiHandler.buildSearchApi()
                username = apiHandler.userName().toString()
                recentlyPlayed = apiHandler.userRecentlyPlayed()
                suggested = arrayListOf()
                favoriteGenre = apiHandler.userTopGenres()
                favoriteArtist = arrayListOf()
                favoriteTracks = arrayListOf()
                insertDB(username, recentlyPlayed, suggested, favoriteGenre, favoriteArtist, favoriteTracks)
            }
        }catch (e: Exception){
            println(e)
        }
    }

    fun getRecentlyPlayed(): List<PlayHistory> {
        return recentlyPlayed
    }

    fun getUsername(): String {
        return username
    }

    @Suppress("UNUSED_VARIABLE","UNUSED_PARAMETER")
    private fun insertDB(username: String, recentlyPlayed: List<PlayHistory>, suggested: List<Track>, favoriteGenre: ArrayList<String>, favoriteArtist: List<Artist>, favoriteTracks: List<Track>){
        val spotifyDatabase = SpotifyDatabase.getInstance(this)
        val spotifyDataDao = spotifyDatabase.spotifyDataDao
        val repo = SpotifyDataRepository(spotifyDataDao)
        val viewModelFactory = HomeViewModelFactory(repo)
        val spotifyDataEntity : SpotifyDataEntity = SpotifyDataEntity()
        val myViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        myViewModel.username.value = username
        myViewModel.recentlyPlayed.value = recentlyPlayed
        myViewModel.favGenre.value = favoriteGenre

//        spotifyDataEntity.username = username
//        spotifyDataEntity.recentlyPlayed = Gson().toJson(recentlyPlayed)
//        spotifyDataEntity.suggested = Gson().toJson(suggested)
//        spotifyDataEntity.favoriteGenre = Gson().toJson(favoriteGenre)
//        spotifyDataEntity.favoriteArtist = Gson().toJson(favoriteArtist)
//        myViewModel.insert(spotifyDataEntity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("displayName", username)
    }

    fun setMenuTitle(username: String){
        navView.menu.findItem(R.id.navigation_home).title = username
    }

}