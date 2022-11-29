package com.example.spotifytracker.ui.stats

import android.animation.LayoutTransition
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotifytracker.R
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val statsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()


        val layout = binding.statsLayout
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        TransitionManager.beginDelayedTransition(layout, AutoTransition())

        val textView: TextView = binding.textStats
        statsViewModel.text.observe(viewLifecycleOwner) {
           textView.text = it
        }

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
                        "R",
                        randomInt1,
                        Color.parseColor("#FFA726")))
        pc!!.addPieSlice(
            PieModel(
                        "Python",
                        randomInt2,
                        Color.parseColor("#66BB6A")))
        pc!!.addPieSlice(
                     PieModel(
                        "C++",
                         randomInt3,
                        Color.parseColor("#EF5350")))
        pc!!.addPieSlice(
                     PieModel(
                        "Java",
                         randomInt4,
                        Color.parseColor("#29B6F6")))
        pc!!.addPieSlice(
             PieModel(
                    "Java",
                    randomInt5,
                    Color.parseColor("#29B6F6")))

        val lineChart : LineChart = binding.statsGraph
        setChart(lineChart)

        return root
    }

    private fun setChart(lineChart: LineChart){
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}