package com.example.spotifytracker.ui.home

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
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.*
import com.example.spotifytracker.adapters.ArtistListAdapter
import com.example.spotifytracker.adapters.GenreListAdapter
import com.example.spotifytracker.adapters.SongListAdapter
import com.example.spotifytracker.adapters.TrackListAdapter
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataEntity
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.databinding.FragmentHomeBinding
import com.example.spotifytracker.settings.SettingsActivity


class HomeFragment : Fragment(), AdapterView.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.


    private val binding get() = _binding!!
    private lateinit var spotifyDatabase: SpotifyDatabase
    private lateinit var spotifyDataDao: SpotifyDataDao
    private lateinit var repo: SpotifyDataRepository
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var myViewModel: HomeViewModel
    private lateinit var spotifyDataEntity: List<SpotifyDataEntity>
    private lateinit var songArrayList: ArrayList<PlayHistory>
    private lateinit var genreArrayList: ArrayList<String>
    lateinit var songListAdapter: SongListAdapter
    private lateinit var genreListAdapter: GenreListAdapter
    private lateinit var recentlyPlayedList: ListView
    private lateinit var favGenreList: ListView

    private lateinit var artistListAdapter: ArtistListAdapter
    private lateinit var artistArrayList: ArrayList<Artist>
    private lateinit var favArtistList: ListView

    private lateinit var favTrackListAdapter: TrackListAdapter
    private lateinit var favTrackArrayList: ArrayList<Track>
    private lateinit var favTrackList: ListView
    private lateinit var scrollView: NestedScrollView
    private var switchingView: Boolean = true
    private lateinit var myActivity : MainActivity
    private lateinit var sharedSettings: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        spotifyDatabase = SpotifyDatabase.getInstance(requireActivity())
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = HomeViewModelFactory(repo)
        myViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity

        sharedSettings = PreferenceManager.getDefaultSharedPreferences(myActivity)

        binding.recentlyPlayedList.onItemClickListener = this
        binding.favTrackList.onItemClickListener = this
        binding.favArtistList.onItemClickListener = this
        binding.favGenreList.onItemClickListener = this

        collapseAnimationInit()
        homeObservers()
        applySettings()
        swipeRefresh()
        scrollOnChangeListener()

        return root
    }

    private fun collapseAnimationInit() {
        val layout = binding.homeLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
    }

    private fun swipeRefresh() {
        scrollView = binding.nestedScrollView
        val swipeLayout: SwipeRefreshLayout = binding.root.findViewById(R.id.swipe_layout)
        swipeLayout.setOnRefreshListener {
            myActivity.apiBuilder()
            swipeLayout.isRefreshing = false
        }
    }

    private fun scrollOnChangeListener() {
        scrollView = binding.nestedScrollView
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

    private fun homeObservers() {
        recentlyPlayedList = binding.recentlyPlayedList
        favGenreList = binding.favGenreList
        favTrackList = binding.favTrackList
        favArtistList = binding.favArtistList

        genreArrayList = ArrayList()
        songArrayList = ArrayList()
        favTrackArrayList = ArrayList()
        artistArrayList = ArrayList()
        artistListAdapter = ArtistListAdapter(requireActivity(), artistArrayList)
        favTrackListAdapter = TrackListAdapter(requireActivity(), favTrackArrayList)
        songListAdapter = SongListAdapter(requireActivity(), songArrayList)
        genreListAdapter = GenreListAdapter(requireActivity(), genreArrayList)
        favArtistList.adapter = artistListAdapter
        favTrackList.adapter = favTrackListAdapter
        recentlyPlayedList.adapter = songListAdapter
        favGenreList.adapter = genreListAdapter

        myViewModel.username.observe(viewLifecycleOwner) {
            //(activity as AppCompatActivity?)!!.supportActionBar!!.title = it //top action bar
            myActivity.setMenuTitle(it) //bottom nav bar
        }
        //recentlyPlayedList.emptyView = listEmpty()
        myViewModel.recentlyPlayed.observe(viewLifecycleOwner) { it ->
            if(it.isEmpty()){
                val emptyListAdapter = GenreListAdapter(requireActivity(), ArrayList<String>())
                recentlyPlayedList.adapter = emptyListAdapter
                emptyListAdapter.replace(arrayListOf("None Found"))
                emptyListAdapter.notifyDataSetChanged()
                setListViewHeightBasedOnChildren(recentlyPlayedList)
            }else{
                songListAdapter.replace(it)
                songListAdapter.notifyDataSetChanged()
                songArrayList = it as ArrayList<PlayHistory>
                setListViewHeightBasedOnChildren(recentlyPlayedList)
            }
        }

        myViewModel.favGenre.observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                genreListAdapter.replace(arrayListOf("None Found"))
                genreListAdapter.notifyDataSetChanged()
                genreArrayList = it as ArrayList<String>
                setListViewHeightBasedOnChildren(favGenreList)
            }else{
                genreListAdapter.replace(it)
                genreListAdapter.notifyDataSetChanged()
                genreArrayList = it as ArrayList<String>
                setListViewHeightBasedOnChildren(favGenreList)
            }
        }

        myViewModel.favArtist.observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                val emptyListAdapter = GenreListAdapter(requireActivity(), ArrayList<String>())
                favArtistList.adapter = emptyListAdapter
                emptyListAdapter.replace(arrayListOf("None Found"))
                emptyListAdapter.notifyDataSetChanged()
                setListViewHeightBasedOnChildren(favTrackList)
            }else{
                artistListAdapter.replace(it)
                artistListAdapter.notifyDataSetChanged()
                artistArrayList = it as ArrayList<Artist>
                setListViewHeightBasedOnChildren(favArtistList)
            }
        }

        myViewModel.favTrack.observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                val emptyListAdapter = GenreListAdapter(requireActivity(), ArrayList<String>())
                favTrackList.adapter = emptyListAdapter
                emptyListAdapter.replace(arrayListOf("None Found"))
                emptyListAdapter.notifyDataSetChanged()
                setListViewHeightBasedOnChildren(favTrackList)
            }else{
                favTrackListAdapter.replace(it)
                favTrackListAdapter.notifyDataSetChanged()
                favTrackArrayList = it as ArrayList<Track>
                setListViewHeightBasedOnChildren(favTrackList)
            }
        }

        //        myViewModel.allLiveData.observe(requireActivity(), Observer { it ->
//            println("debug: DB Size: " + it.size)
//            if(it.isNotEmpty()){
//                spotifyDataEntity = it
//
//                val myRecentlyPlayed : List<PlayHistory> = Gson().fromJson(spotifyDataEntity.last().recentlyPlayed, object : TypeToken<List<PlayHistory>>() {}.type)
//                songListAdapter.notifyDataSetChanged()
//            }
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when(p0?.id){
            recentlyPlayedList.id -> {
                if (songArrayList.isNotEmpty()){
                    val hyperlink = songArrayList[p2].track.externalUrls.spotify
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
                    startActivity(intent)
                }
            }
            favTrackList.id -> {
                if (favTrackArrayList.isNotEmpty()){
                    val hyperlink = favTrackArrayList[p2].externalUrls.spotify
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
                    startActivity(intent)
                }
            }
            favArtistList.id -> {
                if (artistArrayList.isNotEmpty()){
                    val hyperlink = artistArrayList[p2].externalUrls.spotify
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
                    startActivity(intent)
                }
            }
        }

    }

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
        binding.homeLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(binding.homeLayout, AutoTransition())
        switchingView = true
        //(activity as AppCompatActivity?)!!.supportActionBar!!.show()
        // (activity as AppCompatActivity?)!!.supportActionBar!!.title = myViewModel.username.value
    }


    override fun onPause() {
        super.onPause()
        switchingView = false
    }

    private fun applySettings(){
        binding.recentlyPlayedOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().recentlyPlayedVisibilityKey, true)
        binding.favoriteTracksOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteTracksVisibilityKey, true)
        binding.favoriteArtistsOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteArtistsVisibilityKey, true)
        binding.favoriteGenresOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteGenresVisibilityKey, true)
        binding.recentlyPlayedInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().recentlyPlayedCollapseKey, false)
        binding.suggestedInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().suggestedCollapseKey, false)
        binding.favoriteTracksInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteTracksCollapseKey, false)
        binding.favoriteArtistsInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteArtistsCollapseKey, false)
        binding.favoriteGenresInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().favoriteGenresCollapseKey, false)

        MainActivity().changeArrow(binding.favArtistArrow, binding.favoriteArtistsInnerCardview.isVisible)
        MainActivity().changeArrow(binding.favGenreArrow,binding.favoriteGenresInnerCardview.isVisible)
        MainActivity().changeArrow(binding.favTracksArrow,binding.favoriteTracksInnerCardview.isVisible)
        MainActivity().changeArrow(binding.suggestedArrow,binding.suggestedInnerCardview.isVisible)
        MainActivity().changeArrow(binding.recentlyPlayedArrow,binding.recentlyPlayedInnerCardview.isVisible)
    }

}