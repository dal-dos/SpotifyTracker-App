package com.example.spotifytracker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


@Suppress("RedundantExplicitType")
class LoginActivity : AppCompatActivity() {
    private val KEY: String = "KEY"
    private val REQUEST_CODE = 1337
    private val CLIENT_ID = "9609905ad0f54f66b8d574d367aee504"
    private val REDIRECT_URI = "com.example.spotifytracker://callback"
    private val scopes = arrayOf("user-read-recently-played","user-library-modify","user-read-email","user-read-private", "user-follow-read", "playlist-read-private", "playlist-modify-private", "user-top-read")
    private lateinit var spotifyDatabase: SpotifyDatabase
    private lateinit var spotifyDataDao: SpotifyDataDao
    private lateinit var repo: SpotifyDataRepository
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var myViewModel: LoginViewModel
    private lateinit var mySharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title=""

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        spotifyDatabase = SpotifyDatabase.getInstance(this)
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = LoginViewModelFactory(repo)
        myViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        supportActionBar?.hide()

        mySharedPreferences = applicationContext.getSharedPreferences("SPOTIFY", 0)

    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickLogin(view: View) {
        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(scopes)
        val request : AuthorizationRequest = builder.build()
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickSignup(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com/us/signup"))
        startActivity(intent)
    }

    private fun loginSucceeded(){
        val intent : Intent = Intent(this, MainActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putString(KEY, "Key")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
        System.out.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val editor = mySharedPreferences.edit()
                    editor.putString("token", response.accessToken)
                    editor.putString("type", response.type.name)
                    editor.putInt("expires", response.expiresIn)
                    println("debug: Authentication succeeded with token ${response.accessToken}")
                    editor.apply()
                    loginSucceeded()}
                AuthorizationResponse.Type.ERROR -> {
                    println("debug: Error with logging in")}
                else -> {println("debug: Timeout or decline with logging in, response type is: ${response.type}")}
            }
        }
    }


}