package com.example.spotifytracker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myRecycleView: RecyclerView = listView
        myRecycleView.setPadding(0,0,0,-10)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "recently_played_before" -> {
                //do something
                //date dialog
            }
            "recently_played_after" -> {
                //do something
                //date dialog
            }
        }
        return super.onPreferenceTreeClick(preference)
    }


}