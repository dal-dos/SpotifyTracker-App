package com.example.spotifytracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.text.DecimalFormat

class WeatherApiHandler(val context: Context) {
    private val weatherUrl: String = "https://api.openweathermap.org/data/2.5/weather"
    private val weatherApiKey: String = "dc0a87f28557dcf5a3bd4cf16653c722"
    val decimalFormat = DecimalFormat("#.##")
    lateinit var location: Location

    @SuppressLint("MissingPermission")
    fun startApi(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { currLocation: Location? ->
                //get the latitude and longitude
                println("debug: location is lat ${currLocation?.latitude} and long ${currLocation?.longitude}")
                location = currLocation!!
            }
    }


}