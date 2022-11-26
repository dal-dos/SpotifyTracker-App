package com.example.spotifytracker

import android.icu.util.Calendar
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

object Song {
    var title = ""
    var artists : ArrayList<Artist> = arrayListOf()
    var genre : ArrayList<String> = arrayListOf()
    var length : Duration = 3.minutes
    var release : Calendar = Calendar.getInstance()
}