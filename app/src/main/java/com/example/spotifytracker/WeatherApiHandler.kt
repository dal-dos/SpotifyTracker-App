package com.example.spotifytracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.text.DecimalFormat
import java.util.*

class WeatherApiHandler(val context: Context) {
    private var weatherUrl: String = "https://api.openweathermap.org/data/2.5/weather"
    private val weatherApiKey: String = "dc0a87f28557dcf5a3bd4cf16653c722"
    val decimalFormat = DecimalFormat("#.##")
    lateinit var location: Location
    lateinit var cityName: String

    @SuppressLint("MissingPermission")
    fun startApi(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { currLocation: Location? ->
                //get the latitude and longitude
                //println("debug: location is lat ${currLocation?.latitude} and long ${currLocation?.longitude}")
                location = currLocation!!
                val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                cityName = addresses[0].locality
                val country = addresses[0].countryCode
                println("debug: location is $cityName")
                weatherUrl = "$weatherUrl?q=$cityName,$country&appid=$weatherApiKey"
                getWeather()
            }
    }

    // Function is not working correctly when called from another class
    private fun getWeather() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val url: String = weatherUrl
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.POST, url,
            { response ->
                //get the JSON object
                println("debug: Open weather API response with code $response")
            },
            //In case of any error
            { error -> println("debug: Open weather API error with code $error")})
        queue.add(stringReq)
    }


}