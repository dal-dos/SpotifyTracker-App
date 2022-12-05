@file:Suppress("RedundantExplicitType")

package com.example.spotifytracker.ui.stats

import android.animation.LayoutTransition
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.database.SpotifyDataDao
import com.example.spotifytracker.database.SpotifyDataRepository
import com.example.spotifytracker.database.SpotifyDatabase
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.example.spotifytracker.settings.SettingsActivity
import com.example.spotifytracker.ui.home.HomeViewModel
import com.example.spotifytracker.ui.home.HomeViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlin.Float.Companion.POSITIVE_INFINITY
import kotlin.collections.ArrayList
import kotlin.time.Duration.Companion.hours


class StatsFragment : Fragment(), OnChartValueSelectedListener {

    private var _binding: FragmentStatsBinding? = null
    private lateinit var scrollView: NestedScrollView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var pc: PieChart
    private lateinit var timeListenedPieChart: PieChart
    private lateinit var sharedSettings: SharedPreferences
    private lateinit var myActivity : MainActivity
    private var switchingView: Boolean = true
    //database stuff(expletive)
    private lateinit var spotifyDatabase: SpotifyDatabase
    private lateinit var spotifyDataDao: SpotifyDataDao
    private lateinit var repo: SpotifyDataRepository
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var myViewModel: HomeViewModel
    private var totalPopularity : Float = 0F
    private var totalListened : Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root = startFunction(inflater,container)

        collapseAnimationInit()

        statsObservers()

        scrollView = binding.statsNestedScrollView
        scrollOnChangeListener()
        applySettings()

        swipeRefresh()

        return root
    }

    private fun startFunction(inflater: LayoutInflater, container: ViewGroup?): View {
        initViewModel()
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity
        sharedSettings = PreferenceManager.getDefaultSharedPreferences(myActivity)
        return root
    }

    private fun initViewModel() {
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

        myViewModel.playedWeekHistory.observe(viewLifecycleOwner) {
            setChart(it)
        }

        myViewModel.timePlayedDay.observe(viewLifecycleOwner) {
            setTimePlayedDayChart(it)
        }

        myViewModel.favTrack.observe(viewLifecycleOwner) {
            makeListenedPieChart(it)
        }
    }



    private fun collapseAnimationInit() {
        val layout = binding.statsLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
    }

    private fun makePopularityPieChart(arrayList : List<Artist>) {
        val artistViews : ArrayList<View> = arrayListOf(binding.artist1View,binding.artist2View,binding.artist3View,binding.artist4View,binding.artist5View)
        val artistTexts : ArrayList<TextView> = arrayListOf(binding.artist1Text,binding.artist2Text,binding.artist3Text,binding.artist4Text,binding.artist5Text)
        artistViews.map { it.isVisible = false }
        artistTexts.map { it.isVisible = false }
        pc = binding.popularityPieChart
        val tf : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaalight)
        val legend: Legend = pc!!.legend
        legend.isEnabled = false
//        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
//        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        legend.orientation = Legend.LegendOrientation.VERTICAL
//        legend.isWordWrapEnabled = true
//        legend.setDrawInside(false)
//        legend.textColor= Color.WHITE
//        legend.textSize = 12F
//        legend.typeface = tf
//        legend.xOffset = -30F

        val colors : ArrayList<Int> = arrayListOf(Color.parseColor("#FFA726"), Color.parseColor("#66BB6A"), Color.parseColor("#EF5350"),Color.parseColor("#29B6F6"), Color.parseColor("#FF6200EE"))
        val dataEntries : ArrayList<PieEntry> = arrayListOf()

        for (i in arrayList.indices) {
            if (i == 5){
                break
            }
            val name = arrayList[i].name
            artistTexts[i].isVisible = true
            artistViews[i].isVisible = true
            artistTexts[i].isSelected = true
            val popularity = arrayList[i].popularity.toFloat()
            totalPopularity += popularity
            dataEntries.add(PieEntry(popularity,name))
            artistTexts[i].text = name
        }

        if (arrayList.isEmpty()) {
            artistTexts[0].text = "No Data Available"
            artistTexts[0].isVisible = true
            artistViews[0].isVisible = true
            artistTexts[0].isSelected = true
            dataEntries.add(PieEntry(100f,"No Data Available"))
        }

        val pieDataSet = PieDataSet(dataEntries, "")
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        pieData.dataSetLabels
        pieData.setValueTextSize(12F)
        pieData.setValueTypeface(tf)

        pc!!.data = pieData
        pc!!.setHoleColor(Color.parseColor("#2E6943"))
        pc!!.setDrawEntryLabels(false)
        pc!!.description.isEnabled= false
        pc!!.setDrawCenterText(true)
        pc!!.setOnChartValueSelectedListener(this)
        pc!!.setCenterTextTypeface(tf)
        pc!!.centerText = "Click a slice"
        pc!!.setCenterTextColor(Color.WHITE)

        pc!!.invalidate()
        pc!!.animateY(1400,Easing.EaseInOutQuad)

    }

    private fun makeListenedPieChart(arrayList : List<Track>) {
        val artistViews : ArrayList<View> = arrayListOf(binding.statsArtist1View,binding.statsArtist2View,binding.statsArtist3View,binding.statsArtist4View,binding.statsArtist5View)
        val artistTexts : ArrayList<TextView> = arrayListOf(binding.statsArtist1Text,binding.statsArtist2Text,binding.statsArtist3Text,binding.statsArtist4Text,binding.statsArtist5Text)
        artistViews.map { it.isVisible = false }
        artistTexts.map { it.isVisible = false }
        timeListenedPieChart = binding.statsPieChart
        val tf : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaalight);
        val legend: Legend = timeListenedPieChart!!.legend
        legend.isEnabled = false

        val colors : ArrayList<Int> = arrayListOf(Color.parseColor("#FF018786"),
            Color.parseColor("#fb7268"),
            Color.parseColor("#9C0700"),
            Color.parseColor("#FF000000"),
            Color.parseColor("#FFA726"))
        val dataEntries : ArrayList<PieEntry> = arrayListOf()

        for (i in arrayList.indices) {
            if (i == 5){
                break
            }
            val name = arrayList[i].name
            artistTexts[i].isVisible = true
            artistViews[i].isVisible = true
            artistTexts[i].isSelected = true
            val listened = arrayList[i].durationMs.toFloat()
            totalListened += listened
            dataEntries.add(PieEntry(listened,name))
            artistTexts[i].text = name
        }

        if (arrayList.isEmpty()) {
            artistTexts[0].text = "No Data Available"
            artistTexts[0].isVisible = true
            artistViews[0].isVisible = true
            artistTexts[0].isSelected = true
            dataEntries.add(PieEntry(100f,"No Data Available"))
        }

        val pieDataSet = PieDataSet(dataEntries, "")
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        pieData.dataSetLabels
        pieData.setValueTextSize(12F)
        pieData.setValueTypeface(tf)

        timeListenedPieChart!!.data = pieData
        timeListenedPieChart!!.setHoleColor(Color.parseColor("#2E6943"))
        timeListenedPieChart!!.setDrawEntryLabels(false)
        timeListenedPieChart!!.description.isEnabled= false
        timeListenedPieChart!!.setDrawCenterText(true)
        timeListenedPieChart!!.setOnChartValueSelectedListener(this)
        timeListenedPieChart!!.setCenterTextTypeface(tf)
        timeListenedPieChart!!.centerText = "Click a slice"
        timeListenedPieChart!!.setCenterTextColor(Color.WHITE)

        timeListenedPieChart!!.invalidate()
        timeListenedPieChart!!.animateY(1400,Easing.EaseInOutQuad)

    }


    private fun setTimePlayedDayChart(rp: List<PlayHistory>) {
        val gData : MutableList<Entry> = mutableListOf()
        val lineChart : LineChart = binding.timePlayedDayChart
        //Chart configurations
        //Fake data to temporarily display chart
        val timePlayHistory : ArrayList<Float> = arrayListOf(0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F)
        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        var timeType = 60000
//        if(sharedSettings.getString(SettingsActivity.hoursPlayedWeekTimeType, "hours") != "hours"){
//            timeType = 60000
//        }

        for (i in rp.indices) {
            val calendar : Calendar = Calendar.getInstance()
            val playedAtTime = rp[i].playedAt
            calendar.time = sdf.parse(playedAtTime)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            println("time: " + hour)
            val trackLengthMilliseconds = rp[i].track.length.toFloat()
            val trackLengthMinutes = trackLengthMilliseconds/timeType
            println(hour)
            timePlayHistory[hour] = trackLengthMinutes + timePlayHistory[hour]
        }


        for (i in 1..24){
            gData.add(Entry(i.toFloat(), timePlayHistory[i-1]))
        }

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
        lineChart.xAxis.axisMaximum= 24F
        lineChart.xAxis.axisMinimum= 1F
        lineChart.xAxis.labelCount = 24
        lineChart.xAxis.granularity = 4F
        lineChart.xAxis.setDrawLabels(true)
        lineChart.axisLeft.axisMinimum = 0F
        lineChart.axisLeft.granularity = 1F
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisRight.setDrawZeroLine(true)
        lineChart.legend.isEnabled = false
        lineChart.invalidate()

        lineChart.animateY(1300)
    }

    private fun setChart(rp: List<PlayHistory>) {
        val gData : MutableList<Entry> = mutableListOf()
        val lineChart : LineChart = binding.statsGraph
        //Chart configurations
        //Fake data to temporarily display chart
        val timePlayHistory : ArrayList<Float> = arrayListOf(0F,0F,0F,0F,0F,0F,0F)
        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        var timeType = 3600000
        if(sharedSettings.getString(SettingsActivity.hoursPlayedWeekTimeType, "hours") != "hours"){
            timeType = 60000
        }

        for (i in rp.indices) {
            val calendar : Calendar = Calendar.getInstance()
            val playedAtDate = rp[i].playedAt
            calendar.time = sdf.parse(playedAtDate)
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            val trackLengthMilliseconds = rp[i].track.length.toFloat()
            val trackLengthMinutes = trackLengthMilliseconds/timeType
            timePlayHistory[day-1] = trackLengthMinutes + timePlayHistory[day-1]
        }

        for (i in 1..7){
            gData.add(Entry(i.toFloat(), timePlayHistory[i-1]))
        }

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
        lineChart.legend.isEnabled = false
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
        binding.timePlayedDayOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.timePlayedDayVisibilityKey, true)
        binding.statsPieChartOuterCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.statsPieChartVisibilityKey, true)

        binding.popularityPieChartInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.popularityPieChartCollapseKey, true)
        binding.hoursPlayedWeekInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.hoursPlayedWeekCollapseKey, true)
        binding.timePlayedDayInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.timePlayedDayCollapseKey, true)
        binding.statsPieChartInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity.statsPieChartCollapseKey, true)

        MainActivity().changeArrow(binding.popularityPieChartArrow, binding.popularityPieChartInnerCardview.isVisible)
        MainActivity().changeArrow(binding.hoursPlayedWeekArrow, binding.hoursPlayedWeekInnerCardview.isVisible)
        MainActivity().changeArrow(binding.timePlayedDayArrow, binding.timePlayedDayInnerCardview.isVisible)
        MainActivity().changeArrow(binding.statsPieChartArrow, binding.statsPieChartInnerCardview.isVisible)
    }

    private fun scrollOnChangeListener() {
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
    /*override fun onResume() {
        super.onResume()
        switchingView = true
    }
    override fun onPause() {
        super.onPause()
        switchingView = false
    }*/

    override fun onResume() {
        super.onResume()
        binding.statsLayout.isVisible = true
        myActivity.showActionBar(true)
    }

    private fun swipeRefresh() {
        val swipeLayout: SwipeRefreshLayout = binding.root.findViewById(R.id.swipe_layout)
        swipeLayout.setOnRefreshListener {
            myActivity.apiBuilder()
            swipeLayout.isRefreshing = false
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val raw = e!!.y
        if(raw != POSITIVE_INFINITY){
            if(raw <= 100){
                val tfbold : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaabold)
                val tflight : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaalight)
                val artistTexts : ArrayList<TextView> = arrayListOf(binding.artist1Text,binding.artist2Text,binding.artist3Text,binding.artist4Text,binding.artist5Text)
                val data = String.format("%.1f",(raw/totalPopularity)*100)
                val index : Int = h!!.x.toInt()
                onNothingSelected()
                artistTexts.map{ it.typeface = tflight}
                artistTexts[index].typeface = tfbold
                pc.centerText = "$data%"
            } else {
                val tfbold : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaabold)
                val tflight : Typeface? = ResourcesCompat.getFont(myActivity.applicationContext, R.font.comfortaalight);
                val artistTexts : ArrayList<TextView> = arrayListOf(binding.statsArtist1Text,binding.statsArtist2Text,binding.statsArtist3Text,binding.statsArtist4Text,binding.statsArtist5Text)
                val data = String.format("%.2f", raw/3600000)
                val index : Int = h!!.x.toInt()
                artistTexts.map{ it.typeface = tflight}
                artistTexts[index].typeface = tfbold
                timeListenedPieChart.centerText = "$data hrs"
            }
        }

    }

    override fun onNothingSelected() {
        pc.centerText = ""
        timeListenedPieChart.centerText = ""
    }

    override fun onPause() {
        super.onPause()
        binding.statsLayout.isVisible = false
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.scrollTo(0,0)
    }
}