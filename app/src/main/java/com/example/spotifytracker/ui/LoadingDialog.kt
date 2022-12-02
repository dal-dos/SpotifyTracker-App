package com.example.spotifytracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.spotifytracker.R
import kotlinx.coroutines.*

class LoadingDialog(apiBuilderLoad: Job) : DialogFragment()  {
    private val apiBuilderLoad = apiBuilderLoad
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.loading_screen, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.setOnShowListener {
            CoroutineScope(Dispatchers.Default).launch {
                while(apiBuilderLoad.isActive){
                    //nothing
                }
                dialog.dismiss()
            }
        }
        return dialog
    }
}