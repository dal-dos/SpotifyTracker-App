package com.example.spotifytracker

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class LoginActivity : AppCompatActivity() {
    private val KEY: String = "KEY"
    private val REQUEST_CODE = 1337
    private val CLIENT_ID = "9609905ad0f54f66b8d574d367aee504"
    private val REDIRECT_URI = "http://localhost:8888/callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title=""

    }

    fun onClickLogin(view: View) {
        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

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
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {loginSucceeded()}
                AuthorizationResponse.Type.ERROR -> {
                    println("debug: Error with logging in")}
                else -> {println("debug: Timeout or decline with logging in, response type is: ${response.type}")}
            }
        }
    }
}