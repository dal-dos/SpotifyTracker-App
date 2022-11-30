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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.example.spotifytracker.settings.SettingsActivity
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private lateinit var scrollView: NestedScrollView
    var gData : MutableList<Entry> = mutableListOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var artist1: TextView? = null
    var artist2:TextView? = null
    var artist3:TextView? = null
    var artist4:TextView? = null
    var artist5 :TextView? = null
    var pc: PieChart? = null
    private lateinit var sharedSettings: SharedPreferences
    private lateinit var myActivity : MainActivity
    private lateinit var myViewModel : StatsViewModel
    private var switchingView: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        myViewModel = ViewModelProvider(this)[StatsViewModel::class.java]
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myActivity = requireActivity() as MainActivity

        //if (savedInstanceState == null){
        //    myActivity.supportActionBar!!.show()
        //}
        //myActivity.showActionBar(true)

        sharedSettings = PreferenceManager.getDefaultSharedPreferences(myActivity)

        collapseAnimationInit()
        statsObservers()
        makePopularityPieChart()
        setChart()
        //scrollOnChangeListener()
        applySettings()
        swipeRefresh()
        return root
    }

    private fun statsObservers() {
        myViewModel.text.observe(viewLifecycleOwner) {
             //
        }
    }

    private fun collapseAnimationInit() {
        val layout = binding.statsLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
    }

    private fun makePopularityPieChart() {

        artist1 = binding.artist1Text
        artist2 = binding.artist2Text
        artist3 = binding.artist3Text
        artist4 = binding.artist4Text
        artist4 = binding.artist5Text
        pc = binding.popularityPieChart

        var randomInt1 : Float = 40.0F
        var randomInt2 : Float = 40.0F
        var randomInt3 : Float = 40.0F
        var randomInt4 : Float = 40.0F
        var randomInt5 : Float = 40.0F

        pc!!.innerPaddingColor = Color.parseColor("#2E6943")

        pc!!.addPieSlice(
            PieModel(
                "Artist1",
                randomInt1,
                Color.parseColor("#FFA726")))
        pc!!.addPieSlice(
            PieModel(
                "Artist2",
                randomInt2,
                Color.parseColor("#66BB6A")))
        pc!!.addPieSlice(
            PieModel(
                "Artist3",
                randomInt3,
                Color.parseColor("#EF5350")))
        pc!!.addPieSlice(
            PieModel(
                "Artist4",
                randomInt4,
                Color.parseColor("#29B6F6")))
        pc!!.addPieSlice(
            PieModel(
                "Artist5",
                randomInt5,
                Color.parseColor("#29B6F6")))

        pc!!.startAnimation();

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
        binding.popularityPieChartInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().popularityPieChartCollapseKey, true)
        binding.hoursPlayedWeekInnerCardview.isVisible = sharedSettings.getBoolean(SettingsActivity().hoursPlayedWeekCollapseKey, true)

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