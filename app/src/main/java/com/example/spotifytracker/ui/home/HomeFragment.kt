package com.example.spotifytracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adamratzman.spotify.models.PlayHistory
import com.example.spotifytracker.*
import com.example.spotifytracker.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

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
    lateinit var songListAdapter: SongListAdapter
    private lateinit var recentlyPlayedList: ListView
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

        val textView: TextView = binding.textHome
        homeViewModel.username.observe(viewLifecycleOwner) {
            textView.text = it
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = it
        }

        recentlyPlayedList = binding.recentlyPlayedList
        songArrayList = ArrayList()
        songListAdapter = SongListAdapter(requireActivity(), songArrayList)
        recentlyPlayedList.adapter = songListAdapter

        myViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]

        myViewModel.recentlyPlayed.observe(viewLifecycleOwner) { it ->
            songListAdapter.replace(it)
            songListAdapter.notifyDataSetChanged()
        }

//        myViewModel.allLiveData.observe(requireActivity(), Observer { it ->
//            println("debug: DB Size: " + it.size)
//            if(it.isNotEmpty()){
//                spotifyDataEntity = it
//                val myRecentlyPlayed = Gson().fromJson<List<PlayHistory>>(spotifyDataEntity.last().recentlyPlayed, object : TypeToken<List<PlayHistory?>?>() {}.type)
//                songListAdapter.replace(myRecentlyPlayed)
//                songListAdapter.notifyDataSetChanged()
//            }
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}