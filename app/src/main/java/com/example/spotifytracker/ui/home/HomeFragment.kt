package com.example.spotifytracker.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamratzman.spotify.models.PlayHistory
import com.example.spotifytracker.*
import com.example.spotifytracker.databinding.FragmentHomeBinding

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
    lateinit var genreListAdapter: GenreListAdapter
    private lateinit var recentlyPlayedList: ListView
    private lateinit var favGenreList: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        spotifyDatabase = SpotifyDatabase.getInstance(requireActivity())
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = HomeViewModelFactory(repo)
        val homeViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val myActivity = requireActivity() as MainActivity

        homeViewModel.username.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = it
            myActivity.setMenuTitle(it)
        }

        recentlyPlayedList = binding.recentlyPlayedList
        favGenreList = binding.favGenreList
        genreArrayList = ArrayList()
        songArrayList = ArrayList()
        songListAdapter = SongListAdapter(requireActivity(), songArrayList)
        genreListAdapter = GenreListAdapter(requireActivity(), genreArrayList)
        recentlyPlayedList.adapter = songListAdapter
        favGenreList.adapter = genreListAdapter

        recentlyPlayedList.onItemClickListener = this

        myViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]

        myViewModel.recentlyPlayed.observe(viewLifecycleOwner) { it ->
            songListAdapter.replace(it)
            songListAdapter.notifyDataSetChanged()
            songArrayList = it as ArrayList<PlayHistory>
        }

        myViewModel.favGenre.observe(viewLifecycleOwner) {
            genreListAdapter.replace(it)
            genreListAdapter.notifyDataSetChanged()
            genreArrayList = it as ArrayList<String>
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

        val swipeLayout: SwipeRefreshLayout = root.findViewById(R.id.swipe_layout)
        swipeLayout.setOnRefreshListener {
            myActivity.apiBuilder()
            swipeLayout.isRefreshing = false
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val hyperlink = songArrayList[p2].track.externalUrls.spotify
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
        startActivity(intent)
    }
}