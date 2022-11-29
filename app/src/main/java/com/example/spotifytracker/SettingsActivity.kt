package com.example.spotifytracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    val recentlyPlayedNumberOfItemsKey = "recently_played_number_of_items"
    val favoriteTracksNumberOfItemsKey = "favorite_tracks_number_of_items"
    val favoriteArtistsNumberOfItemsKey = "favorite_artists_number_of_items"
    val favoriteGenresNumberOfItemsKey = "favorite_genres_number_of_items"
    val recentlyPlayedVisibilityKey = "recently_played_visibility"
    val favoriteTracksVisibilityKey = "favorite_tracks_visibility"
    val favoriteArtistsVisibilityKey = "favorite_artists_visibility"
    val favoriteGenresVisibilityKey = "favorite_genres_visibility"
    val recentlyPlayedCollapseKey = "recently_played_collapse"
    val suggestedCollapseKey = "suggested_collapse"
    val favoriteTracksCollapseKey = "favorite_tracks_collapse"
    val favoriteArtistsCollapseKey = "favorite_artists_collapse"
    val favoriteGenresCollapseKey = "favorite_genres_collapse"
    val popularityPieChartCollapseKey = "popularity_pie_chart_collapse"
    val favoriteTracksTimeRange = "favorite_tracks_time_range"
    val favoriteArtistsTimeRange ="favorite_artists_time_range"
    val favoriteGenresTimeRange ="favorite_genres_time_range"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}