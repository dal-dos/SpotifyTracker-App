package com.example.spotifytracker.ui.home

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var genreListAdapter: GenreListAdapter
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

        myViewModel.username.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = it //top action bar
            myActivity.setMenuTitle(it) //bottom nav bar
        }
        //recentlyPlayedList.emptyView = listEmpty()
        myViewModel.recentlyPlayed.observe(viewLifecycleOwner) { it ->
            if(it.isEmpty()){
                val emptyListAdapter = GenreListAdapter(requireActivity(), genreArrayList)
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = myViewModel.username.value
    }
}