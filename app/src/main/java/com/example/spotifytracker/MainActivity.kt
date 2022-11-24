package com.example.spotifytracker

// Spotify API

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.Token
import com.example.spotifytracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CLIENT_ID = "9609905ad0f54f66b8d574d367aee504"
    private val REDIRECT_URI = "com.example.spotifytracker://callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var mySharedPreferences: SharedPreferences
    private lateinit var apiHandler: SpotifyApiHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)
        var accessToken = mySharedPreferences.getString("token", "")
        var type = mySharedPreferences.getString("type", "")
        var expires = mySharedPreferences.getInt("expires", 9999)
        var token: Token = Token(accessToken!!, type!!, expires)
        apiHandler = SpotifyApiHandler(token)
        lifecycleScope.launch {
            apiHandler.buildSearchApi()
        }

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


//        //Setting connection parameters
//        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
//            .setRedirectUri(REDIRECT_URI)
//            .showAuthView(true)
//            .build()
//
//        //App Remote to interact with spotify
//        SpotifyAppRemote.connect(this, connectionParams,
//            object : Connector.ConnectionListener {
//                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
//                    mSpotifyAppRemote = spotifyAppRemote
//                    Log.d("MainActivity", "Connected! Yay!")
//
//                    connected()
//                }
//
//                override fun onFailure(throwable: Throwable) {
//                    Log.e("MainActivity", throwable.message, throwable)
//
//                    // Something went wrong when attempting to connect! Handle errors here
//                }
//            })
       // CoroutineScope(Dispatchers.IO).launch{
        //    apiHandler.userTopTracks()
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    fun onClickLogout(view: View) {
        AuthorizationClient.clearCookies(this)
        val intent : Intent = Intent(this, LoginActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putString("Temporary", "Key")
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
//        val request: AuthorizationRequest = Builder()
//            .addHeader("Authorization", "Basic : $encodedInput")
//            .url("https://api.spotify.com/v1/artists/41X1TR6hrK8Q2ZCpp2EqCz")
//            .addHeader("Authorization", "Bearer $token")
//            .addHeader("Accept", "application/json")
//            .addHeader("Content-Type", "application/json")
//            .build()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun connected(){

    }
}