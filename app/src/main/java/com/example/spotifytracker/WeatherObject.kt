package com.example.spotifytracker

data class WeatherObject(val weather: String, val temp: Double, val weatherDesc: String, val time: String, val icon: String){
    override fun toString(): String {
        return "debug: Weather is: $weather temp is $temp on $time"
    }
}