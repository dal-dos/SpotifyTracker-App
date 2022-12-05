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
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*


class WeatherApiHandler(val context: Context) {
    private var weatherUrlForCurr: String = "https://api.openweathermap.org/data/2.5/weather"
    private var weatherUrl: String = "https://api.openweathermap.org/data/2.5/forecast?"
    private val weatherApiKey: String = "dc0a87f28557dcf5a3bd4cf16653c722"
    private lateinit var location: Location
    private lateinit var cityName: String
    private lateinit var futureWeatherArray: ArrayList<WeatherObject>
    private lateinit var currWeather: WeatherObject
    private lateinit var queue: RequestQueue
    private var counter: Int = 0

    @SuppressLint("MissingPermission")
    suspend fun startApi(): Boolean{
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val temp = fusedLocationProviderClient.lastLocation
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
            }
        while (!temp.isComplete){
            delay(500)
        }
        return temp.isComplete
    }

    // Function is not working correctly when called from another class
    suspend fun setFutureWeather() {
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
                    val iconUrl = "http://openweathermap.org/img/wn/"+ jsonObjectWeather.getString("icon") + "@4x.png"
                    val jsonObjectMain: JSONObject = daysWeatherObject.getJSONObject("main")
                    val temp = jsonObjectMain.getDouble("temp") - 273.15 // minus this to convert to Celsius from kelvin
                    val time = daysWeatherObject.getString("dt_txt")
                    val date = time.split(" ")[0]
                    iterator += 1
                    if (date == lastCheckedDate){
                        continue
                    }
                    val newWeatherObject: WeatherObject = WeatherObject(currWeather, temp, description, date, iconUrl)
                    lastCheckedDate = date
                    futureWeatherArray.add(newWeatherObject)
                    println(newWeatherObject)
                    counter = 2
                }
            },
            //In case of any error
            { error -> println("debug: Open weather API error with code $error")})
        queue.add(stringReq)
        while (counter != 2){
            delay(500)
        }
    }

    // Function is not working correctly when called from another class
    suspend fun setCurrWeather() {
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
                val iconUrl = "https://openweathermap.org/img/wn/"+ jsonObjectWeather.getString("icon") + "@4x.png"
                val daysWeather = jsonObjectWeather.getString("main")
                val description = jsonObjectWeather.getString("description")
                val jsonObjectMain: JSONObject = jsonWeather.getJSONObject("main")
                val temp = jsonObjectMain.getDouble("temp") - 273.15 // minus this to convert to Celsius from kelvin
                val time = jsonWeather.getString("dt")
                currWeather = WeatherObject(daysWeather, temp, description, time, iconUrl)
                println("Curr weather is $currWeather")
                counter = 1
            },
            //In case of any error
            { error -> println("debug: Open weather API error with code $error")})
        queue.add(stringReq)
        while (counter != 1){
            delay(500)
        }
        //println("Curr weather is set")
    }

    suspend fun getCurrentWeather(): WeatherObject {
        while (counter != 1){
            delay(100)
        }
        return currWeather
    }

    suspend fun getFutureWeather(): ArrayList<WeatherObject> {
        while (counter != 2){
            delay(100)
        }
        return futureWeatherArray
    }

    fun getCity(): String {
        return cityName
    }

}