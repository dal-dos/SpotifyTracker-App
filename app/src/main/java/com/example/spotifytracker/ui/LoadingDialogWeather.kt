package com.example.spotifytracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.WeatherObject
import kotlinx.coroutines.*

class LoadingDialogWeather(
    private val futureWeather: MutableLiveData<ArrayList<WeatherObject>>,
    private val mainActivity: MainActivity,
    private val myOrientation: Int
) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.loading_screen, null)
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = "Loading Weather Data…"
        val progressBar: ProgressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.weather_blue), PorterDuff.Mode.SRC_IN )
        builder.setView(view)
        val dialog = builder.create()
        dialog.setOnShowListener {
            CoroutineScope(Dispatchers.Default).launch {
                while(futureWeather.value.isNullOrEmpty()){
                    delay(500)
                }
                dialog.dismiss()
                delay(500)
                mainActivity.requestedOrientation = myOrientation
            }
        }
        return dialog
    }
}