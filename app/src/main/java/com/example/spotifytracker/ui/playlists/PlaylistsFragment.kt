package com.example.spotifytracker.ui.playlists

import android.animation.LayoutTransition
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.AdapterView
import android.widget.ListView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamratzman.spotify.models.Playlist
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.adapters.GenreListAdapter
import com.example.spotifytracker.adapters.PlaylistListAdapter
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.databinding.FragmentPlaylistsBinding
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class PlaylistsFragment : Fragment(), AdapterView.OnItemClickListener {

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
    private lateinit var playlistArrayList :ArrayList<Playlist>
    private lateinit var recommendedTodayArrayList :ArrayList<Playlist>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myActivity = requireActivity() as MainActivity
        val root = startFunction(inflater,container)

        initializeOnItemClickListeners()
        collapseAnimationInit()
        mainImage = root.findViewById(R.id.imageView)
        currentWeatherTv = root.findViewById(R.id.weather_text)
        currWeatherDescTv = root.findViewById(R.id.weather_text_desc)
        cityTextView = root.findViewById(R.id.weather_text_city)
        tempTv = root.findViewById(R.id.weather_temp)
        playlistsObservers()
        //scrollOnChangeListener()
        swipeRefresh()
        return root
    }

    private fun initializeOnItemClickListeners() {
        binding.allPlaylistsList.onItemClickListener = this
    }

    private fun startFunction(inflater: LayoutInflater, container: ViewGroup?): View {
        initViewModel()
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity
        return root
    }

    private fun playlistsObservers() {
        playlistArrayList = ArrayList()
        val allPlaylistsList = binding.allPlaylistsList
        val playlistListAdapter: PlaylistListAdapter = PlaylistListAdapter(requireActivity(), playlistArrayList)
        allPlaylistsList.adapter = playlistListAdapter

        recommendedTodayArrayList = ArrayList()
        val recommendedTodayList = binding.recommendedTodayPlaylistList
        val recommendedTodayListAdapter: PlaylistListAdapter = PlaylistListAdapter(requireActivity(), recommendedTodayArrayList)
        recommendedTodayList.adapter = recommendedTodayListAdapter


        myViewModel.allPlaylists.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                val emptyListAdapter = GenreListAdapter(requireActivity(), ArrayList())
                allPlaylistsList.adapter = emptyListAdapter
                emptyListAdapter.replace(arrayListOf("None Found"))
                emptyListAdapter.notifyDataSetChanged()
                setListViewHeightBasedOnChildren(allPlaylistsList)
            }else{
                playlistListAdapter.replace(it)
                playlistListAdapter.notifyDataSetChanged()
                playlistArrayList = it as ArrayList<Playlist>
                setListViewHeightBasedOnChildren(allPlaylistsList)
            }
        }

        myViewModel.city.observe(viewLifecycleOwner) {
            cityTextView.text = it
        }

        myViewModel.futureWeather.observe(viewLifecycleOwner) {
            //setChart(it)
        }

        myViewModel.currWeather.observe(viewLifecycleOwner) {
            val currWeather = it.weather
            currentWeatherTv.text = currWeather
            currWeatherDescTv.text = it.weatherDesc.split(' ').joinToString(" ") {words ->
                words.replaceFirstChar {firstLetter->
                    firstLetter.uppercase()
                }
            }
            Picasso.get().load(it.icon).into(mainImage)
            tempTv.text = it.temp.roundToInt().toString() + "°C"

            setRecommendedTodayPlaylistList(currWeather, recommendedTodayList, recommendedTodayListAdapter)
        }


    }

    private fun setRecommendedTodayPlaylistList(
        currWeather: String,
        recommendedTodayList: ListView,
        recommendedTodayListAdapter: PlaylistListAdapter
    ) {
        val IDmap = MainActivity.mapOfPlaylistIds
        var index = 0
        val currWeatherPlaylistIndices = arrayListOf<Int>()
        for(item in IDmap){
            if(item.value == currWeather){
                currWeatherPlaylistIndices.add(index)
            }
            index += 1
        }
        val randomNumber = currWeatherPlaylistIndices.random()

        val currWeatherPlaylistArrayList = listOf(playlistArrayList[currWeatherPlaylistIndices[randomNumber]])

        if(!IDmap.containsValue(currWeather)){
            val emptyListAdapter = GenreListAdapter(requireActivity(), ArrayList())
            recommendedTodayList.adapter = emptyListAdapter
            emptyListAdapter.replace(arrayListOf("None Found"))
            emptyListAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(recommendedTodayList)
        }else{
            recommendedTodayListAdapter.replace(currWeatherPlaylistArrayList)
            recommendedTodayListAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(recommendedTodayList)
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

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return
        var totalHeight = listView.paddingTop + listView.paddingBottom
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            (listItem as? ViewGroup)?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            listItem.measure(binding.root.measuredWidth, binding.root.measuredHeight)

            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }

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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0?.id) {
         binding.allPlaylistsList.id -> {
             if (playlistArrayList.isNotEmpty()){
                 val hyperlink = playlistArrayList[p2].externalUrls.spotify
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
                 startActivity(intent)
             }
         }
        }
    }
}