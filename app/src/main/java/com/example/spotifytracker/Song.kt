package com.example.spotifytracker

import android.icu.util.Calendar
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

object Song {
    val title = ""
    val artists : ArrayList<Artist> = arrayListOf()
    val genre : ArrayList<String> = arrayListOf()
    val length : Duration = 3.minutes
    val release : Calendar = Calendar.getInstance()
}