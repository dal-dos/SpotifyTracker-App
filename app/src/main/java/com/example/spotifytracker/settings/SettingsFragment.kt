package com.example.spotifytracker.settings

import android.app.DatePickerDialog
import android.content.Context
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
import java.util.*

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
                showDateDialog("recently_played_before")
            }
            "recently_played_after" -> {
                //do something
                //date dialog
                showDateDialog("recently_played_after")
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
            "reset" -> {
                val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                sharedSettings.edit().clear().apply()
                requireActivity().onBackPressed()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun showDateDialog(timeContext: String){
        val calendar = Calendar.getInstance()
        val datePickerDialog: DatePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener{ view2, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            val editor = sharedSettings.edit()
            if (timeContext == SettingsActivity.recentlyPlayedBefore){
                val afterPreference = findPreference<Preference>(SettingsActivity.recentlyPlayedAfter)
                afterPreference?.shouldDisableView = true
                afterPreference?.isEnabled = false
                editor.remove(SettingsActivity.recentlyPlayedAfter)
            } else {
                val beforePreference = findPreference<Preference>(SettingsActivity.recentlyPlayedBefore)
                beforePreference?.shouldDisableView = true
                beforePreference?.isEnabled = false
                editor.remove(SettingsActivity.recentlyPlayedBefore)
            }

            editor.putString(timeContext, calendar.timeInMillis.toString())
            editor.apply()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }


}