package com.example.spotifytracker.ui.Weather

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.spotifytracker.R
import com.example.spotifytracker.databinding.ActivityWeatherApiHandlerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject

class WeatherApiHandler : AppCompatActivity() {
// *** https://www.geeksforgeeks.org/how-to-build-a-weather-app-in-android/


    //weather url to get JSON
    var weather_url1 = "https://api.open-meteo.com/v1/forecast?latitude=49.25&longitude=-123.12&hourly=temperature_2m,precipitation&timezone=America%2FLos_Angeles"
    //api id for url
    var api_id1 = "6432d1c22b32445a809871688125500b"
    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        println("DEBUG: MADE IT INTO WEATHER API HANDLER")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //link the textView in which the temperature will be displayed
        textView = findViewById(R.id.current_weather_textview)
        //create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        obtainLocation()
        Log.e("lat", weather_url1)
        //on clicking this button function to get the coordinates will be called
//        btVar1.setOnClickListener {
//            Log.e("lat", "onClick")
//            //function to find the coordinates of the last location
//            obtainLocation()
//        }

    }
    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        Log.e("lat", "function")
        //get the last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //get the latitute and longitude and create the http URL
               // weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude +"&lon="+ location?.longitude + "&key="+ api_id1
               weather_url1 ="https://api.open-meteo.com/v1/forecast?latitude=49.25&longitude=-123.12&hourly=temperature_2m,precipitation&timezone=America%2FLos_Angeles"
                Log.e("lat", weather_url1.toString())
                //this function will fetch data from URL
                getTemp()
            }
    }

    fun getTemp() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.e("lat", response.toString())
                //get the JSON object
                val obj = JSONObject(response)
                //get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())
                //get the JSON object from the array at index position 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())
                //set the temperature and the city name using getString() function
                textView.text = obj2.getString("temp")+" deg Celcius in "+obj2.getString("city_name")
            },
            //In case of any error
            { textView!!.text = "That didn't work!" })
        queue.add(stringReq)
    }
}