package com.example.spotifytracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.loading_screen, null)
        builder.setView(view)
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