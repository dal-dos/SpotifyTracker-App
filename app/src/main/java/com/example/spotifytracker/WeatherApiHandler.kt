package com.example.spotifytracker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*


class WeatherApiHandler(val context: Context) {
    private var weatherUrlForCurr: String = "https://api.openweathermap.org/data/2.5/weather"
    private var weatherUrl: String = "https://api.openweathermap.org/data/2.5/forecast?"
    private val weatherApiKey: String = "dc0a87f28557dcf5a3bd4cf16653c722"
    private lateinit var location: Location
    lateinit var cityName: String
    lateinit var futureWeatherArray: ArrayList<WeatherObject>
    lateinit var currWeather: WeatherObject
    private lateinit var queue: RequestQueue

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
                weatherUrlForCurr = "$weatherUrlForCurr?q=$cityName,$country&appid=$weatherApiKey"
                weatherUrl = weatherUrl + "lat=" + location.latitude + "&lon=" + location.longitude + "&appid=" + weatherApiKey
                // Instantiate the RequestQueue.
                queue = Volley.newRequestQueue(context)
                getCurrWeather()
                getFutureWeather()
            }
    }

    // Function is not working correctly when called from another class
    private fun getFutureWeather() {
        val url: String = weatherUrl
        futureWeatherArray = arrayListOf()
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.POST, url,
            { response ->
                //get the JSON object
                println("debug: Open weather API response with output: ")
                Log.d("response", response)
                val jsonWeather: JSONObject = JSONObject(response)
                val allWeather = jsonWeather.getJSONArray("list")
                var iterator = 0
                var lastCheckedDate = ""
                while(iterator < allWeather.length()){
                    val daysWeatherObject = allWeather.getJSONObject(iterator)
                    val jsonArray: JSONArray = daysWeatherObject.getJSONArray("weather")
                    val jsonObjectWeather = jsonArray.getJSONObject(0)
                    val currWeather = jsonObjectWeather.getString("main")
                    val description = jsonObjectWeather.getString("description")
                    val jsonObjectMain: JSONObject = daysWeatherObject.getJSONObject("main")
                    val temp = jsonObjectMain.getDouble("temp") - 273.15 // minus this to convert to Celsius from kelvin
                    val time = daysWeatherObject.getString("dt_txt")
                    val date = time.split(" ")[0]
                    iterator += 1
                    if (date == lastCheckedDate){
                        continue
                    }
                    val newWeatherObject: WeatherObject = WeatherObject(currWeather, temp, description, date)
                    lastCheckedDate = date
                    futureWeatherArray.add(newWeatherObject)
                    println(newWeatherObject)
                }
            },
            //In case of any error
            { error -> println("debug: Open weather API error with code $error")})
        queue.add(stringReq)
    }

    // Function is not working correctly when called from another class
    private fun getCurrWeather() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val url: String = weatherUrlForCurr
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.POST, url,
            { response ->
                //get the JSON object
                println("debug: Open weather API response for current weather with output: ")
                Log.d("response", response)
                val jsonWeather: JSONObject = JSONObject(response)
                val jsonArray: JSONArray = jsonWeather.getJSONArray("weather")
                val jsonObjectWeather = jsonArray.getJSONObject(0)
                val daysWeather = jsonObjectWeather.getString("main")
                val description = jsonObjectWeather.getString("description")
                val jsonObjectMain: JSONObject = jsonWeather.getJSONObject("main")
                val temp = jsonObjectMain.getDouble("temp") - 273.15 // minus this to convert to Celsius from kelvin
                val time = jsonWeather.getString("dt")
                currWeather = WeatherObject(daysWeather, temp, description, time)
                println("Curr weather is $currWeather")
            },
            //In case of any error
            { error -> println("debug: Open weather API error with code $error")})
        queue.add(stringReq)
    }


}