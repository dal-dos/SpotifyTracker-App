package com.example.spotifytracker.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R

class SettingsActivity : AppCompatActivity() {
    companion object{
        const val hoursPlayedWeekTimeType = "hours_played_week_time_type"
        const val recentlyPlayedNumberOfItemsKey = "recently_played_number_of_items"
        const val favoriteTracksNumberOfItemsKey = "favorite_tracks_number_of_items"
        const val favoriteArtistsNumberOfItemsKey = "favorite_artists_number_of_items"
        const val favoriteGenresNumberOfItemsKey = "favorite_genres_number_of_items"
        const val suggestedNumberOfItemsKey = "suggested_number_of_items"

        const val recentlyPlayedVisibilityKey = "recently_played_visibility"
        const val favoriteTracksVisibilityKey = "favorite_tracks_visibility"
        const val favoriteArtistsVisibilityKey = "favorite_artists_visibility"
        const val favoriteGenresVisibilityKey = "favorite_genres_visibility"
        const val suggestedVisibilityKey = "suggested_visibility"
        const val hoursPlayedWeekVisibilityKey = "hours_played_week_visibility"
        const val popularityPieChartVisibilityKey = "popularity_pie_chart_visibility"
        const val timePlayedDayVisibilityKey = "time_played_day_visibility"

        const val recentlyPlayedCollapseKey = "recently_played_collapse"
        const val suggestedCollapseKey = "suggested_collapse"
        const val favoriteTracksCollapseKey = "favorite_tracks_collapse"
        const val favoriteArtistsCollapseKey = "favorite_artists_collapse"
        const val favoriteGenresCollapseKey = "favorite_genres_collapse"
        const val hoursPlayedWeekCollapseKey = "hours_played_week_collapse_key"
        const val popularityPieChartCollapseKey = "popularity_pie_chart_collapse"
        const val timePlayedDayCollapseKey = "time_played_day_collapse"

        const val favoriteTracksTimeRangeKey = "favorite_tracks_time_range"
        const val favoriteArtistsTimeRangeKey ="favorite_artists_time_range"
        const val favoriteGenresTimeRangeKey ="favorite_genres_time_range"

        const val recentlyPlayedBeforeKey = "recently_played_before"
        const val recentlyPlayedAfterKey = "recently_played_after"
        const val logoutKey = "logout"
        const val resetKey = "reset"
    }
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
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}