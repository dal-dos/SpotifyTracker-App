package com.example.spotifytracker

// Spotify API

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.databinding.ActivityMainBinding
import com.example.spotifytracker.ui.LoadingDialog
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.coroutines.launch
import org.eazegraph.lib.charts.PieChart


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
    private lateinit var mToolbar : androidx.appcompat.widget.Toolbar
    private var mTouchPosition: Float? = null
    private var mReleasePosition: Float? = null
    private lateinit var sharedSettings : SharedPreferences
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedSettings = PreferenceManager.getDefaultSharedPreferences(this)
        if(savedInstanceState == null) {
            firstTimeStart()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        mToolbar = findViewById(R.id.toolbar)

        setSupportActionBar(findViewById(R.id.toolbar))
        //setupActionBarWithNavController(navController, appBarConfiguration)
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

        //this.supportActionBar?.hide()//hides action bars
        //val actionBar = supportActionBar
        //actionBar?.setDisplayShowCustomEnabled(true);
        //actionBar?.setDisplayShowTitleEnabled(false);
        //setActionBarTitle()
        //this.supportActionBar?.setShowHideAnimationEnabled(true)
        val refreshButton: ImageButton = findViewById(R.id.toolbar_button_refresh)
        val settingsButton: ImageButton = findViewById(R.id.toolbar_button_settings)

        refreshButton.setOnClickListener {
            //finish()
            //startActivity(intent)
            apiBuilder()
            //Toast.makeText(baseContext, "Refreshed Spotify Data", Toast.LENGTH_SHORT).show()
            findViewById<ImageButton>(R.id.toolbar_button_refresh).animate().rotationBy(360F)
        }

        settingsButton.setOnClickListener {
            //finish()
            //startActivity(intent)
            //Toast.makeText(baseContext, "Refreshed Spotify Data", Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(this, SettingsActivity::class.java)
            findViewById<ImageButton>(R.id.toolbar_button_settings).animate().rotationBy(360F)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun firstTimeStart() {
        apiBuilder()
    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.custom_actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option1 -> {
                //finish()
                //startActivity(intent)
                apiBuilder()
                //Toast.makeText(baseContext, "Refreshed Spotify Data", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.option1).animate().rotationBy(360F)
            }
        }
        return super.onOptionsItemSelected(item)
    }*/

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
        val mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)
        mySharedPreferences.edit().clear().apply()
        startActivity(intent)
        finishAffinity()
    }

    fun apiBuilder(){
        openDialog()
        val mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)
        val accessToken = mySharedPreferences.getString("token", "")
        val type = mySharedPreferences.getString("type", "")
        val expires = mySharedPreferences.getInt("expires", 9999)
        val token: Token = Token(accessToken!!, type!!, expires)
        try {
            apiHandler = SpotifyApiHandler(token, sharedSettings)
            lifecycleScope.launch() {
                apiHandler.buildSearchApi()
                username = apiHandler.userName().toString()
                recentlyPlayed = apiHandler.userRecentlyPlayed()
                suggested = arrayListOf()
                favoriteGenre = apiHandler.userTopGenres()
                favoriteArtist = apiHandler.userTopArtists()
                favoriteTracks = apiHandler.userTopTracks()
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
        myViewModel.favArtist.value = favoriteArtist
        myViewModel.favTrack.value = favoriteTracks


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
        val arrow = findViewById<TextView>(R.id.recently_played_arrow)
        var bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        cv.isVisible = bool
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().recentlyPlayedCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewSuggested(view: View) {
        val cv = findViewById<CardView>(R.id.suggested_inner_cardview)
        val arrow = findViewById<TextView>(R.id.suggested_arrow)
        var bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        cv.isVisible = bool
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().suggestedCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewFavoriteTracks(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_tracks_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_tracks_arrow)
        var bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        cv.isVisible = bool
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().favoriteTracksCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewFavoriteArtists(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_artists_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_artist_arrow)
        var bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        cv.isVisible = bool
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().favoriteArtistsCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewFavoriteGenres(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_genres_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_genre_arrow)
        var bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().favoriteGenresCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewPopularityPieChart(view: View) {
        val cv = findViewById<CardView>(R.id.popularity_pie_chart_inner_cardview)
        val arrow = findViewById<TextView>(R.id.popularity_pie_chart_arrow)
        var bool = true
        if (cv.isVisible){
            bool = false
            arrow.animate().rotation(90f)
            val piechart : PieChart = findViewById(R.id.popularity_pie_chart)
            piechart.startAnimation()
        }
        else{
            bool = true
            arrow.animate().rotation(0f)
        }
        cv.isVisible = bool
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity().popularityPieChartCollapseKey,bool)
        editor.apply()
    }

    fun changeArrow(arrow: TextView, bool: Boolean) {
        if(bool){
            arrow.rotation = 90F
            arrow.animate().rotation(0F)
        } else{
            arrow.rotation = 0F
            arrow.animate().rotation(90F)
        }
    }


private fun showActionBar() {
        val mToolbarHeight = mToolbar.height
        val mAnimDuration = 600 /* milliseconds */
        var mVaActionBar: ValueAnimator  = ValueAnimator.ofInt(0, mToolbarHeight)
        if (mVaActionBar != null && mVaActionBar.isRunning()) {
            // we are already animating a transition - block here
            return
        }

        // restore `Toolbar's` height
        mVaActionBar = ValueAnimator.ofInt(0, mToolbarHeight)
        mVaActionBar.addUpdateListener(AnimatorUpdateListener { animation -> // update LayoutParams
            (mToolbar.getLayoutParams() as AppBarLayout.LayoutParams).height =
                (animation.animatedValue as Int)
            mToolbar.requestLayout()
        })
        mVaActionBar.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (supportActionBar != null) { // sanity check
                    println("debug: In the sanity check for show")
                    supportActionBar!!.show()
                }
            }
        })
        mVaActionBar.setDuration(mAnimDuration.toLong())
        mVaActionBar.start()
    }



}