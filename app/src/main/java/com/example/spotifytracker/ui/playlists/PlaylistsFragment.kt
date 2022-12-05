package com.example.spotifytracker.ui.playlists

import android.animation.LayoutTransition
import android.content.SharedPreferences
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.databinding.FragmentPlaylistsBinding
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private lateinit var scrollView: NestedScrollView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pc: PieChart? = null
    private lateinit var sharedSettings: SharedPreferences
    private lateinit var myActivity : MainActivity
    private var switchingView: Boolean = true
    //database stuff(expletive)
    private lateinit var spotifyDatabase: SpotifyDatabase
    private lateinit var spotifyDataDao: SpotifyDataDao
    private lateinit var repo: SpotifyDataRepository
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var myViewModel: HomeViewModel
    private lateinit var mainImage: ImageView
    private lateinit var cityTextView: TextView
    private lateinit var currentWeatherTv: TextView
    private lateinit var currWeatherDescTv: TextView
    private lateinit var tempTv: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myActivity = requireActivity() as MainActivity
        val root = startFunction(inflater,container)

        collapseAnimationInit()
        mainImage = root.findViewById(R.id.imageView)
        currentWeatherTv = root.findViewById(R.id.weather_text)
        currWeatherDescTv = root.findViewById(R.id.weather_text_desc)
        cityTextView = root.findViewById(R.id.weather_text_city)
        tempTv = root.findViewById(R.id.weather_temp)
        statsObservers()
        //scrollOnChangeListener()
        swipeRefresh()
        return root
    }

    private fun startFunction(inflater: LayoutInflater, container: ViewGroup?): View {
        initViewModel()
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity
        return root
    }

    private fun statsObservers() {

        myViewModel.city.observe(viewLifecycleOwner) {
           cityTextView.text = it
        }

        myViewModel.futureWeather.observe(viewLifecycleOwner) {
            //setChart(it)
        }

        myViewModel.currWeather.observe(viewLifecycleOwner) {
            currentWeatherTv.text = it.weather
            currWeatherDescTv.text = it.weatherDesc.split(' ').joinToString(" ") {
                it.replaceFirstChar {
                    it.uppercase()
                }
            }
            Picasso.get().load(it.icon).into(mainImage)
            tempTv.text = it.temp.roundToInt().toString() + "°C"
        }
    }

    private fun initViewModel() {
        spotifyDatabase = SpotifyDatabase.getInstance(requireActivity())
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = HomeViewModelFactory(repo)
        myViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
    }

    private fun collapseAnimationInit() {
        val layout = binding.playlistLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
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

    private fun swipeRefresh() {
        val swipeLayout: SwipeRefreshLayout = binding.root.findViewById(R.id.swipe_layout)
        swipeLayout.setOnRefreshListener {
            myActivity.apiBuilder()
            swipeLayout.isRefreshing = false
        }
    }
}