package com.example.spotifytracker.ui.playlists

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var scrollView: NestedScrollView
    private var switchingView: Boolean = true
    private lateinit var myActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val layout = binding.playlistLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())

        myActivity = requireActivity() as MainActivity

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        //scrollOnChangeListener()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClickLogout(view: View) {}

/*    private fun scrollOnChangeListener() {
        scrollView = binding.playlistNestedScrollView
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY + 4 <= oldScrollY ){
                println("debug: Oldscrolly is $oldScrollY and scrolly is $scrollY")
                //scroll up
                //println("debug: Showing the tool bar")
                (activity as AppCompatActivity?)!!.supportActionBar!!.show()
            }
            else if (oldScrollY + 4 <= scrollY){
                //scroll down
                //println("debug: Hiding the tool bar")
                if (switchingView){
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        switchingView = true
    }

    override fun onPause() {
        super.onPause()
        switchingView = false
    }*/

    override fun onResume() {
        super.onResume()
        myActivity.showActionBar(true)
    }
}