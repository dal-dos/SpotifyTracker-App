package com.example.spotifytracker.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R

class SettingsActivity : AppCompatActivity() {
    val recentlyPlayedNumberOfItemsKey = "recently_played_number_of_items"
    val favoriteTracksNumberOfItemsKey = "favorite_tracks_number_of_items"
    val favoriteArtistsNumberOfItemsKey = "favorite_artists_number_of_items"
    val favoriteGenresNumberOfItemsKey = "favorite_genres_number_of_items"
    val suggestedNumberOfItemsKey = "suggested_number_of_items"
    val recentlyPlayedVisibilityKey = "recently_played_visibility"
    val favoriteTracksVisibilityKey = "favorite_tracks_visibility"
    val favoriteArtistsVisibilityKey = "favorite_artists_visibility"
    val favoriteGenresVisibilityKey = "favorite_genres_visibility"
    val suggestedVisibilityKey = "suggested_visibility"
    val recentlyPlayedCollapseKey = "recently_played_collapse"
    val suggestedCollapseKey = "suggested_collapse"
    val favoriteTracksCollapseKey = "favorite_tracks_collapse"
    val favoriteArtistsCollapseKey = "favorite_artists_collapse"
    val favoriteGenresCollapseKey = "favorite_genres_collapse"
    val hoursPlayedWeekCollapseKey = "hours_played_week_collapse_key"
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

        onClickListeners()
    }

    private fun onClickListeners() {
        val saveSettingsButton = findViewById<Button>(R.id.save_settings_button)
        saveSettingsButton.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickSaveSettings(view: View) {
        println("HELLO")
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}