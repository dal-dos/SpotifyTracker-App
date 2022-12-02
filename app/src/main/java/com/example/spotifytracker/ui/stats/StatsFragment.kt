@file:Suppress("RedundantExplicitType")

package com.example.spotifytracker.ui.stats

import android.animation.LayoutTransition
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamratzman.spotify.models.Artist
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.settings.SettingsActivity
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private lateinit var scrollView: NestedScrollView
    private var gData : MutableList<Entry> = mutableListOf()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root = startFunction(inflater,container)

        collapseAnimationInit()

        statsObservers()

        setChart()
        //scrollOnChangeListener()
        applySettings()

        swipeRefresh()

        return root
    }

    private fun startFunction(inflater: LayoutInflater, container: ViewGroup?): View {
        initViemodel()
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity
        sharedSettings = PreferenceManager.getDefaultSharedPreferences(myActivity)
        return root
    }

    private fun initViemodel() {
        spotifyDatabase = SpotifyDatabase.getInstance(requireActivity())
        spotifyDataDao = spotifyDatabase.spotifyDataDao
        repo = SpotifyDataRepository(spotifyDataDao)
        viewModelFactory = HomeViewModelFactory(repo)
        myViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
    }

    private fun statsObservers() {

        myViewModel.favArtist.observe(viewLifecycleOwner) {
            makePopularityPieChart(it)
        }

//        myViewModel.favTrack.observe(viewLifecycleOwner) {
//             for(i in it.indices) {
//                trackArrayList.add(it.get(i))
//            }
//        }

    }

    private fun collapseAnimationInit() {
        val layout = binding.statsLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
    }

    private fun makePopularityPieChart(arrayList : List<Artist>) {
        pc = binding.popularityPieChart
        pc?.clearChart()
        val colors : ArrayList<String> = arrayListOf("#FFA726", "#66BB6A", "#EF5350","#29B6F6", "#FF6200EE")
        val artists : ArrayList<TextView> = arrayListOf(binding.artist1Text,binding.artist2Text,binding.artist3Text,binding.artist4Text,binding.artist5Text)
        val artistViews : ArrayList<View> = arrayListOf(binding.artistColorView1,binding.artistColorView2,binding.artistColorView3,binding.artistColorView4,binding.artistColorView5)

        artists.map { it.isVisible = false }
        artistViews.map { it.isVisible = false}
        for (i in arrayList.indices) {
            if (i == 5){
                break
            }
            artists[i].text = arrayList[i].name
            artists[i].isVisible = true
            artistViews[i].isVisible = true
            pc!!.addPieSlice(PieModel(arrayList[i].name, arrayList[i].popularity.toFloat(), Color.parseColor(colors[i])))
        }
        if (arrayList.isEmpty()) {
            artists[0].text = getString(R.string.noData)
            artists[0].isVisible = true
            artistViews[0].isVisible = true
            pc!!.addPieSlice(PieModel("No Data Available", 100f, Color.parseColor(colors[0])))
        }

        pc!!.innerPaddingColor = Color.parseColor("#2E6943")

        pc!!.startAnimation()
    }

    private fun setChart(){
        val lineChart : LineChart = binding.statsGraph
        //Chart configurations
        //Fake data to temporarily display chart
        gData.add(Entry(1f,1.4f))
        gData.add(Entry(2f,5.2f))
        gData.add(Entry(3f,4.5f))
        gData.add(Entry(4f,2.4f))
        gData.add(Entry(5f,1.9f))
        gData.add(Entry(6f,7.7f))
        gData.add(Entry(7f,3.2f))

        val lineChartData : LineDataSet = LineDataSet(gData,"Spotify")
        val chosenColor : Int = Color.WHITE //color of graph details
        lineChartData.label = ""
        lineChartData.setDrawValues(false)
        lineChartData.setDrawCircles(false)
        lineChartData.valueTextColor = chosenColor
        lineChartData.color = chosenColor
        lineChart.description.textColor = chosenColor
        lineChart.axisLeft.textColor = chosenColor
        lineChart.xAxis.textColor = chosenColor
        lineChart.legend.textColor = chosenColor
        lineChartData.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineChart.data =  LineData(lineChartData)
        lineChart.description.text = ""
        lineChart.xAxis.axisMaximum= 7F
        lineChart.xAxis.axisMinimum= 1F
        lineChart.xAxis.labelCount = 7
        lineChart.xAxis.granularity = 1F
        lineChart.xAxis.setDrawLabels(true)
        lineChart.axisLeft.axisMinimum = 0F
        lineChart.axisLeft.granularity = 1F
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisRight.setDrawZeroLine(true)
        lineChart.invalidate()

        lineChart.animateY(1300)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun applySettings(){
        binding.hoursPlayedWeekOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.hoursPlayedWeekVisibilityKey, true)
        binding.popularityPieChartOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.popularityPieChartVisibilityKey, true)
        binding.popularityPieChartInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.popularityPieChartCollapseKey, true)
        binding.hoursPlayedWeekInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.hoursPlayedWeekCollapseKey, true)

        MainActivity().changeArrow(binding.popularityPieChartArrow, binding.popularityPieChartInnerCardview.isVisible)
        MainActivity().changeArrow(binding.hoursPlayedWeekArrow, binding.hoursPlayedWeekInnerCardview.isVisible)
    }

/*    private fun scrollOnChangeListener() {
        scrollView = binding.statsNestedScrollView
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