package com.example.spotifytracker

// Spotify API

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
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
import com.example.spotifytracker.ui.LoadingDialog
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
    private lateinit var spotifyDatabase : SpotifyDatabase
    private lateinit var spotifyDataDao : SpotifyDataDao
    private lateinit var repo : SpotifyDataRepository
    private lateinit var viewModelFactory : HomeViewModelFactory
    private lateinit var spotifyDataEntity : SpotifyDataEntity
    private lateinit var myViewModel :HomeViewModel

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

        spotifyDatabase = SpotifyDatabase.getInstance(this)
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = HomeViewModelFactory(repo)
        spotifyDataEntity = SpotifyDataEntity()
        myViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]


        if(savedInstanceState != null){
            this.setMenuTitle(myViewModel.username.value.toString())
        }

        if(findViewById<LinearLayout>(R.id.home_layout) != null){
            val layout = findViewById<LinearLayout>(R.id.home_layout)
            layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            TransitionManager.beginDelayedTransition(layout, AutoTransition())
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option1 -> {
                //finish()
                //startActivity(intent)
                apiBuilder()
                //Toast.makeText(baseContext, "Refreshed Spotify Data", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog(){
        val dialog = LoadingDialog()
        dialog.show(supportFragmentManager, "tag")

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

    fun apiBuilder(){
        openDialog()
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

    fun onClickCardViewRecentlyPlayed(view: View) {
        val cv = findViewById<CardView>(R.id.recently_played_inner_cardview)
        cv.isVisible = !cv.isVisible
    }

    fun onClickCardViewSuggested(view: View) {
        val cv = findViewById<CardView>(R.id.suggested_inner_cardview)
        cv.isVisible = !cv.isVisible
    }

    fun onClickCardViewFavoriteTracks(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_tracks_inner_cardview)
        cv.isVisible = !cv.isVisible
    }

    fun onClickCardViewFavoriteArtists(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_artists_inner_cardview)
        cv.isVisible = !cv.isVisible
    }
    fun onClickCardViewFavoriteGenres(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_genres_inner_cardview)
        cv.isVisible = !cv.isVisible
    }

}