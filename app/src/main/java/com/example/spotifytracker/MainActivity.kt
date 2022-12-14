package com.example.spotifytracker

// Spotify API

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.adamratzman.spotify.models.*
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.databinding.ActivityMainBinding
import com.example.spotifytracker.login.LoginActivity
import com.example.spotifytracker.settings.SettingsActivity
import com.example.spotifytracker.ui.LoadingDialog
import com.example.spotifytracker.ui.LoadingDialogWeather
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.example.spotifytracker.ui.playlists.PlaylistsData
import com.example.spotifytracker.ui.playlists.SpotifyPlaylist
import com.github.mikephil.charting.animation.Easing
import android.location.LocationListener
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Suppress("RedundantExplicitType")
class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiHandler: SpotifyApiHandler
    private var username = ""
    private var recentlyPlayed : List<PlayHistory> = arrayListOf()
    private var suggested : List<Track> = arrayListOf()
    private var favoriteGenre : ArrayList<String> = arrayListOf()
    private var favoriteTracks : List<Track> = arrayListOf()
    private var favoriteArtist : List<Artist> = arrayListOf()
    private var playedWeekHistory : List<PlayHistory> = arrayListOf()
    private var timePlayedDay : List<PlayHistory> = arrayListOf()
    private var allPlaylists : List<SpotifyPlaylist> = arrayListOf()
    private lateinit var navView: BottomNavigationView
    private lateinit var spotifyDatabase : SpotifyDatabase
    private lateinit var spotifyDataDao : SpotifyDataDao
    private lateinit var repo : SpotifyDataRepository
    private lateinit var viewModelFactory : HomeViewModelFactory
    private lateinit var spotifyDataEntity : SpotifyDataEntity
    private lateinit var myViewModel :HomeViewModel
    private lateinit var mToolbar : androidx.appcompat.widget.Toolbar
    private lateinit var sharedSettings : SharedPreferences

    private lateinit var initLocation: Location
    private lateinit var locationManager: LocationManager
    private lateinit var weatherCoroutine: Job
    var locationListenerActive: Boolean = false

/*    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val callback = object : LocationCallback() {
        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }

        override fun onLocationResult(result: LocationResult) {
            val lastLocation = result?.lastLocation
            Log.d("location", lastLocation.toString())
            getWeatherData()

            super.onLocationResult(result)
        }
    }*/


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedSettings = PreferenceManager.getDefaultSharedPreferences(this)

        if(savedInstanceState == null) {
            firstTimeStart()
//            checkPermission()
            //Log.d("location", "ONCREATE BEFORE LOCATION")
           // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            //onGPS()
            initLocation = Location("init")
            initLocation.longitude = 0.0
            initLocation.latitude = 0.0
        }
        initLocationManager()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //        val appBarConfiguration = AppBarConfiguration(
        //            setOf(
        //                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        //            )
        //        )
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

        if(savedInstanceState != null){
            this.supportActionBar!!.show()
        }

        //this.supportActionBar?.hide()//hides action bars
        //val actionBar = supportActionBar
        //actionBar?.setDisplayShowCustomEnabled(true);
        //actionBar?.setDisplayShowTitleEnabled(false);
        //setActionBarTitle()
        //this.supportActionBar?.setShowHideAnimationEnabled(true)

        println("Location is: ${initLocation.latitude} and ${initLocation.longitude}")
        onClickListeners()
    }

    fun showActionBar(showing: Boolean){
        if (showing){
            this.supportActionBar!!.show()
        } else {
            this.supportActionBar!!.hide()
        }
    }

    private fun onClickListeners() {
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

    @SuppressLint("MissingPermission")
    private fun initLocationManager() {
        println("DEBUG: INIT LOCATION MANAGER")
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val provider: String? = locationManager.getBestProvider(criteria, true)
        if(provider != null) {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                initLocation = location
            }
            locationManager.requestLocationUpdates(provider, 0, 0f, this)
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

    private fun openDialog(apiBuilderLoad: Job) {
        val myOrientation = requestedOrientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        val dialog = LoadingDialog(apiBuilderLoad,this,myOrientation)
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "tag")
    }

     fun openWeatherDialog(futureWeather: MutableLiveData<ArrayList<WeatherObject>>) {
         val myOrientation = requestedOrientation
         requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
         val dialog = LoadingDialogWeather(futureWeather,this, myOrientation)
         dialog.isCancelable = false
         dialog.show(supportFragmentManager, "tag")
     }

//    @Suppress("UNUSED_PARAMETER")
//    fun onClickLogout(view: View) {
//        AuthorizationClient.clearCookies(this)
//        val intent : Intent = Intent(this, LoginActivity::class.java)
//        val bundle: Bundle = Bundle()
//        bundle.putString("Temporary", "Key")
//        intent.putExtras(bundle)
//        val mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)
//        mySharedPreferences.edit().clear().apply()
//        sharedSettings.edit().clear().apply()
//        startActivity(intent)
//        finishAffinity()
//    }

    fun apiBuilder(){
        println("DEBUG: REFRESH BUTTON CLICKED")
        //might not need
       // weatherApiHandler = WeatherApiHandler(this)
        initLocationManager()

        val mySharedPreferences = applicationContext.getSharedPreferences(LoginActivity().spotifyKey, 0)
        val accessToken = mySharedPreferences.getString(LoginActivity().tokenKey, "")
        val type = mySharedPreferences.getString(LoginActivity().typeKey, "")
        val expires = mySharedPreferences.getInt(LoginActivity().expiresKey, 9999)
        val token: Token = Token(accessToken!!, type!!, expires)
        try {
            apiHandler = SpotifyApiHandler(token, sharedSettings)
            val apiBuilderLoad = lifecycleScope.launch() {
                apiHandler.buildSearchApi()
                username = apiHandler.userName().toString()
                recentlyPlayed = apiHandler.userRecentlyPlayed()
                suggested = apiHandler.userSuggested()
                favoriteGenre = apiHandler.userTopGenres()
                favoriteArtist = apiHandler.userTopArtists()
                favoriteTracks = apiHandler.userTopTracks()
                playedWeekHistory = apiHandler.userPlayedWeekHistory()
                timePlayedDay = apiHandler.userTimePlayedDay()
                allPlaylists = PlaylistsData.allPlaylist
                insertDB(username, recentlyPlayed, suggested, favoriteGenre, favoriteArtist, favoriteTracks, playedWeekHistory, timePlayedDay, allPlaylists)
            }
            openDialog(apiBuilderLoad)
        }catch (e: Exception){
            println(e)
        }
    }

    @Suppress("UNUSED_VARIABLE","UNUSED_PARAMETER")
    private fun insertDB(
        username: String,
        recentlyPlayed: List<PlayHistory>,
        suggested: List<Track>,
        favoriteGenre: ArrayList<String>,
        favoriteArtist: List<Artist>,
        favoriteTracks: List<Track>,
        playedWeekHistory: List<PlayHistory>,
        timePlayedDay: List<PlayHistory>,
        allPlaylists: List<SpotifyPlaylist>
    ){

        myViewModel.username.value = username
        myViewModel.recentlyPlayed.value = recentlyPlayed
        myViewModel.favGenre.value = favoriteGenre
        myViewModel.favArtist.value = favoriteArtist
        myViewModel.favTrack.value = favoriteTracks
        myViewModel.suggested.value = suggested
        myViewModel.playedWeekHistory.value = playedWeekHistory
        myViewModel.timePlayedDay.value = timePlayedDay
        myViewModel.allPlaylists.value = allPlaylists
//        spotifyDataEntity.username = username
//        spotifyDataEntity.recentlyPlayed = Gson().toJson(recentlyPlayed)
//        spotifyDataEntity.suggested = Gson().toJson(suggested)
//        spotifyDataEntity.favoriteGenre = Gson().toJson(favoriteGenre)
//        spotifyDataEntity.favoriteArtist = Gson().toJson(favoriteArtist)
//        myViewModel.insert(spotifyDataEntity)
    }

    private fun insertWeather(todayWeather: WeatherObject, future: ArrayList<WeatherObject>, myCity: String){
        myViewModel.currWeather.value = todayWeather
        myViewModel.futureWeather.value = future
        myViewModel.city.value = myCity
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("displayName", username)
    }

    fun setMenuTitle(username: String){
        navView.menu.findItem(R.id.navigation_home).title = username
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewRecentlyPlayed(view: View) {
        val cv = findViewById<CardView>(R.id.recently_played_inner_cardview)
        val arrow = findViewById<TextView>(R.id.recently_played_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.recentlyPlayedCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewSuggested(view: View) {
        val cv = findViewById<CardView>(R.id.suggested_inner_cardview)
        val arrow = findViewById<TextView>(R.id.suggested_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.suggestedCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewFavoriteTracks(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_tracks_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_tracks_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.favoriteTracksCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewFavoriteArtists(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_artists_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_artist_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.favoriteArtistsCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewFavoriteGenres(view: View) {
        val cv = findViewById<CardView>(R.id.favorite_genres_inner_cardview)
        val arrow = findViewById<TextView>(R.id.fav_genre_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.favoriteGenresCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewPopularityPieChart(view: View) {
        val cv = findViewById<CardView>(R.id.popularity_pie_chart_inner_cardview)
        val arrow = findViewById<TextView>(R.id.popularity_pie_chart_arrow)
        if (cv.isVisible){
            val piechart : PieChart = findViewById(R.id.popularity_pie_chart)
            piechart!!.animateY(1400, Easing.EaseInOutQuad)
        }
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.popularityPieChartCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewHoursPlayedWeekChart(view: View) {
        val cv = findViewById<CardView>(R.id.hours_played_week_inner_cardview)
        val arrow = findViewById<TextView>(R.id.hours_played_week_arrow)
        if(cv.isVisible){
            val chart = findViewById<LineChart>(R.id.statsGraph)
            chart.animateY(1300)
        }
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.hoursPlayedWeekCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewRecommendToday(view: View) {
        val cv = findViewById<CardView>(R.id.recommended_today_inner_cardview)
        val cv2 = findViewById<CardView>(R.id.recommended_today_playlist_cardview)
        val arrow = findViewById<TextView>(R.id.rec_today_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        cv2.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.recommendedTodayCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewRecommendTomorrow(view: View) {
        val cv = findViewById<CardView>(R.id.recommended_tomorrow_inner_cardview)
        val arrow = findViewById<TextView>(R.id.rec_tomorrow_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.recommendedTomorrowCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewAllPlaylists(view: View) {
        val cv = findViewById<CardView>(R.id.all_playlists_inner_cardview)
        val arrow = findViewById<TextView>(R.id.all_playlists_arrow)
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.allPlaylistsCollapseKey,bool)
        editor.apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickCardViewTimePlayedDayChart(view: View) {
        val cv = findViewById<CardView>(R.id.time_played_day_inner_cardview)
        val arrow = findViewById<TextView>(R.id.time_played_day_arrow)
        if(cv.isVisible){
            val chart = findViewById<LineChart>(R.id.time_played_day_chart)
            chart.animateY(1300)
        }
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.timePlayedDayCollapseKey,bool)
        editor.apply()
    }

    fun onClickCardViewStatsPieChart(view: View) {
        val cv = findViewById<CardView>(R.id.stats_pie_chart_inner_cardview)
        val arrow = findViewById<TextView>(R.id.stats_pie_chart_arrow)
        if (cv.isVisible){
            val piechart : PieChart = findViewById(R.id.stats_pie_chart)
            piechart!!.animateY(1400, Easing.EaseInOutQuad)
        }
        val bool = !cv.isVisible
        cv.isVisible = bool
        changeArrow(arrow,cv.isVisible)
        val editor = sharedSettings.edit()
        editor.putBoolean(SettingsActivity.statsPieChartCollapseKey,bool)
        editor.apply()
    }


    fun changeArrow(arrow: TextView, bool: Boolean) {
        if(bool){
            //arrow.rotation = 90F
            arrow.animate().rotation(0F)
        } else{
            //arrow.rotation = 0F
            arrow.animate().rotation(90F)
        }
    }

    private fun getWeatherData(location: Location){
        val weatherThread = Thread(){
            val handler = Handler(Looper.getMainLooper())
            val runnable = Runnable {
                val weatherApiHandler = WeatherApiHandler(this)
                weatherCoroutine = lifecycleScope.launch(){
                    val temp = weatherApiHandler.startApi(location)
                    println("the coroutine returns city bool: $temp")
                    if (temp){
                        val city = weatherApiHandler.getCity()
                        weatherApiHandler.setCurrWeather()
                        val currWeather = weatherApiHandler.getCurrentWeather()
                        weatherApiHandler.setFutureWeather()
                        val futureWeather = weatherApiHandler.getFutureWeather()
                        println("Got all the weather data")
                        insertWeather(currWeather, futureWeather, city)
                    }
                }
               // if (supportFragmentManager.findFragmentById(R.id.navigation_notifications)?.isVisible == true){
               //     openWeatherDialog(weatherCoroutine)
              //  }
            }
            handler.post(runnable)
        }
        weatherThread.start()
    }

    /*fun onGPS() {
        if(!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            Log.d("location", "GET MY LOCATION")
            getMyLocation()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getMyLocation() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION), 0)
        else
            requestLocation()
    }*/

/*    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val requestLocation = LocationRequest()
        requestLocation.priority = LocationRequest.QUALITY_HIGH_ACCURACY
        requestLocation.interval = 0
        requestLocation.fastestInterval = 0
        requestLocation.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(requestLocation, callback, Looper.myLooper())
        Log.d("location", "REQUESTED LOCATION WORKS")

    }*/


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationListenerActive = true
                initLocationManager()
            }
            else {
                println("debug: Permission not granted")
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        println("the location is: ${location.longitude} and ${location.latitude}")
        initLocation = location
        locationManager.removeUpdates(this)
        getWeatherData(location)
    }

}