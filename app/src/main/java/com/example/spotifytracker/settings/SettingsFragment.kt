package com.example.spotifytracker.settings

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
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

    companion object {

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myRecycleView: RecyclerView = listView
        myRecycleView.setPadding(0,0,0,-10)

        loadSettings()
    }

    private fun loadSettings() {
        val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val afterPreference = findPreference<Preference>(SettingsActivity.recentlyPlayedAfterKey)
        val beforePreference = findPreference<Preference>(SettingsActivity.recentlyPlayedBeforeKey)
        if(sharedSettings.getString(SettingsActivity.recentlyPlayedAfterKey, null) != null){
            beforePreference?.shouldDisableView = true
            beforePreference?.isEnabled = false
        }
        if(sharedSettings.getString(SettingsActivity.recentlyPlayedBeforeKey, null) != null){
            afterPreference?.shouldDisableView = true
            afterPreference?.isEnabled = false
        }


    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            SettingsActivity.recentlyPlayedBeforeKey -> {
                //do something
                //date dialog
                showDateDialog(SettingsActivity.recentlyPlayedBeforeKey)
            }
            SettingsActivity.recentlyPlayedAfterKey -> {
                //do something
                //date dialog
                showDateDialog(SettingsActivity.recentlyPlayedAfterKey)
            }
            SettingsActivity.logoutKey -> {
                AuthorizationClient.clearCookies(requireActivity())
                val intent : Intent = Intent(requireActivity(), LoginActivity::class.java)
                val bundle: Bundle = Bundle()
                //bundle.putString("Temporary", "Key")
                //intent.putExtras(bundle)
                val mySharedPreferences = requireActivity().getSharedPreferences(LoginActivity().spotifyKey, 0)
                val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                mySharedPreferences.edit().clear().apply()
                sharedSettings.edit().clear().apply()
                startActivity(intent)
                finishAffinity(requireActivity())
            }
            SettingsActivity.resetKey -> {
                val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                sharedSettings.edit().clear().apply()
                requireActivity().onBackPressed()
                val afterPreference = findPreference<Preference>(SettingsActivity.recentlyPlayedAfterKey)
                val beforePreference = findPreference<Preference>(SettingsActivity.recentlyPlayedBeforeKey)
                afterPreference?.shouldDisableView = false
                afterPreference?.isEnabled = true
                beforePreference?.shouldDisableView = false
                beforePreference?.isEnabled = true
                beforePreference?.title = "Listened to before X"
                beforePreference?.summary = "Music that you listened to before X"
                afterPreference?.title = "Listened to after X"
                afterPreference?.summary = "Music that you listened to after X"
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun showDateDialog(timeContext: String){
        val calendar = Calendar.getInstance()
        val sharedSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val initDate = Calendar.getInstance()
        val afterPreference = findPreference<Preference>(SettingsActivity.recentlyPlayedAfterKey)
        val beforePreference = findPreference<Preference>(SettingsActivity.recentlyPlayedBeforeKey)
        initDate.timeInMillis = sharedSettings.getString(timeContext, Calendar.getInstance().timeInMillis.toString())?.toLong()!!

        val datePickerDialog: DatePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener{ view2, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dateForm : SimpleDateFormat = SimpleDateFormat("MMM dd yyyy")
            val timeOf : String = dateForm.format(calendar)
            val editor = sharedSettings.edit()
            if (timeContext == SettingsActivity.recentlyPlayedBeforeKey){
                afterPreference?.shouldDisableView = true
                afterPreference?.isEnabled = false
                editor.remove(SettingsActivity.recentlyPlayedAfterKey)
                beforePreference?.title = "Listened to before $timeOf"
                beforePreference?.summary = "Music that you listened to before $timeOf"
            } else {
                beforePreference?.shouldDisableView = true
                beforePreference?.isEnabled = false
                editor.remove(SettingsActivity.recentlyPlayedBeforeKey)
                afterPreference?.title = "Listened to after $timeOf"
                afterPreference?.summary = "Music that you listened to after $timeOf"
            }

            editor.putString(timeContext, calendar.timeInMillis.toString())
            editor.apply()
        }, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

        datePickerDialogResetButton(datePickerDialog, sharedSettings, afterPreference, beforePreference)

        datePickerDialog.show()
    }

    fun datePickerDialogResetButton(
        datePickerDialog: DatePickerDialog,
        sharedSettings: SharedPreferences,
        afterPreference: Preference?,
        beforePreference: Preference?
    ) {
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "RESET", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
            val editor = sharedSettings.edit()
            afterPreference?.shouldDisableView = false
            afterPreference?.isEnabled = true
            beforePreference?.shouldDisableView = false
            beforePreference?.isEnabled = true
            beforePreference?.title = "Listened to before X"
            beforePreference?.summary = "Music that you listened to before X"
            afterPreference?.title = "Listened to after X"
            afterPreference?.summary = "Music that you listened to after X"
            editor.remove(SettingsActivity.recentlyPlayedBeforeKey)
            editor.remove(SettingsActivity.recentlyPlayedAfterKey)
            editor.apply()
        })
    }

}