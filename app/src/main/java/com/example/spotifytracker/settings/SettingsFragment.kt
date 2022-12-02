package com.example.spotifytracker.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifytracker.R
import com.example.spotifytracker.login.LoginActivity
import com.spotify.sdk.android.auth.AuthorizationClient

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
            "logout" -> {
                AuthorizationClient.clearCookies(requireActivity())
                val intent : Intent = Intent(requireActivity(), LoginActivity::class.java)
                val bundle: Bundle = Bundle()
                bundle.putString("Temporary", "Key")
                intent.putExtras(bundle)
                val mySharedPreferences = requireActivity().getSharedPreferences(LoginActivity().spotifyKey, 0)
                val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                mySharedPreferences.edit().clear().apply()
                sharedSettings.edit().clear().apply()
                startActivity(intent)
                finishAffinity(requireActivity())
            }
        }
        return super.onPreferenceTreeClick(preference)
    }


}