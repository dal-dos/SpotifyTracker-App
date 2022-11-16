package com.example.spotifytracker

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spotify.android.appremote.api.PlayerApi

import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class LoginActivity : AppCompatActivity() {
    private val KEY: String = "KEY"
    private val REQUEST_CODE = 1337
    private val CLIENT_ID = "9609905ad0f54f66b8d574d367aee504"
    private val REDIRECT_URI = "http://localhost:8888/callback"
    private lateinit var spotifyDatabase: SpotifyDatabase
    private lateinit var spotifyDataDao: SpotifyDataDao
    private lateinit var repo: SpotifyDataRepository
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var myViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title=""

        spotifyDatabase = SpotifyDatabase.getInstance(this)
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = LoginViewModelFactory(repo)
        myViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

    }

    fun onClickLogin(view: View) {
        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-read-private","user-read-recently-played"))
        val request : AuthorizationRequest = builder.build()
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
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
        super.onActivityResult(requestCode, resultCode, data)
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse = AuthorizationClient.getResponse(resultCode, data)
//            print("debug: ")
//            println(response.code)
//            print("debug: ")
//            println(data?.extras?.getString("user-read-recently-played"))
//            print("debug: ")
//            println(data?.`package`)
//            print("debug: ")
//            println(data?.data)
//            print("debug: ")
//            println(data?.dataString)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {loginSucceeded()}
                AuthorizationResponse.Type.ERROR -> {
                    println("debug: Error with logging in")}
                else -> {println("debug: Timeout or decline with logging in, response type is: ${response.type}")}
            }
        }
    }

    fun insertDB(username : String, recentlyPlayed: String, suggested: String, favoriteGenre: String, favoriteArtist : String){
        val spotifyDataEntity : SpotifyDataEntity = SpotifyDataEntity()
        spotifyDataEntity.username = username
        spotifyDataEntity.recentlyPlayed = recentlyPlayed
        spotifyDataEntity.suggested = suggested
        spotifyDataEntity.favoriteGenre = favoriteGenre
        spotifyDataEntity.favoriteArtist = favoriteArtist

        myViewModel.insert(spotifyDataEntity)
    }
}