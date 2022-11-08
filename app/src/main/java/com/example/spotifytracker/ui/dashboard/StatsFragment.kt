package com.example.spotifytracker.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotifytracker.databinding.FragmentStatsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    var gData : MutableList<Entry> = mutableListOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textStats
        statsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val lineChart : LineChart = binding.statsGraph

        var xPos = 0
        for (i in (0..10)){
            gData.add(Entry(xPos.toFloat(),i.toFloat()))
            xPos++
        }

        for (i in (10 downTo 0)){
            gData.add(Entry(xPos.toFloat(),i.toFloat()))
            xPos++
        }

        val lineChartData : LineDataSet = LineDataSet(gData,"Stress Level")
        lineChartData.setDrawValues(false)
        lineChartData.setDrawCircles(false)
        lineChartData.valueTextColor = Color.GREEN
        lineChartData.color = Color.GREEN
        lineChart.description.textColor = Color.GREEN
        lineChart.axisLeft.textColor = Color.GREEN
        lineChart.axisRight.textColor = Color.GREEN
        lineChart.legend.textColor = Color.GREEN
        lineChartData.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineChart.data =  LineData(lineChartData)
        lineChart.description.text = "Stress Levels"
        lineChart.xAxis.axisMaximum= 16F
        lineChart.xAxis.axisMinimum= 1F
        lineChart.xAxis.labelCount = 15
        lineChart.axisLeft.axisMinimum = 0F
        lineChart.axisLeft.granularity = 1F
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisRight.setDrawZeroLine(true)
        lineChart.invalidate()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}