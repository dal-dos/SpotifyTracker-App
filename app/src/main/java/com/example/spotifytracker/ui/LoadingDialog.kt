package com.example.spotifytracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import kotlinx.coroutines.*

class LoadingDialog(
    private val apiBuilderLoad: Job,
    private val mainActivity: MainActivity,
    private val myOrientation: Int
) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var counter = 0
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.loading_screen, null)
        builder.setView(view)
        val textView: TextView = view.findViewById(R.id.textView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val dialog = builder.create()
        dialog.setOnShowListener {
            CoroutineScope(Dispatchers.Default).launch {
                while(apiBuilderLoad.isActive){
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