package com.example.tracker.ui.countries

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Constants.DAY_MONTH
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.fromMillis
import com.example.tracker.Utils.ExpansionUtils.toMillis
import com.example.tracker.model.Historic
import com.example.tracker.ui.MainActivity
import com.example.tracker.view.MarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main.*


class CountryDetailFragment : Fragment() {
    private lateinit var mViewModel: CountriesViewModel
    private lateinit var mLineChart: LineChart
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_detail_country, container, false)
        init(v)
        return v
    }

    fun init(v: View) {
        mViewModel = ViewModelProvider(requireActivity() as MainActivity).get(CountriesViewModel::class.java)
        mRecyclerView = v.findViewById(R.id.statistic_days)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL , false)

        mLineChart = v.findViewById(R.id.line_chart)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            (requireContext() as MainActivity).toolbar.title = it.getString(Constants.COUNTRIES_TITLE)
            val historicData =  it.get(Constants.HISTORIC_OF_COUNTRIES) as Historic

            historicData.let  {
                initLineChart(it)
                mRecyclerView.adapter = CountriesDaysAdapter(it)
                mRecyclerView.adapter!!.notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("ResourceType")
    private fun initLineChart(historic: Historic) {
        val xAxisLabel = ArrayList<String>(historic.timeLine.cases.size)
        val cases = ArrayList<Entry>()
        val deaths = ArrayList<Entry>()
        val recovered = ArrayList<Entry>()

        for ((key, value) in historic.timeLine.cases) {
            val x_points = key.toMillis().toFloat()
            val y_points: Float = value.toFloat()
            cases.add(
                Entry(
                    x_points,
                    y_points,
                    null,
                    Triple<Long, Int, String>(key.toMillis(), value, Constants.CASES)
                )
            )
            xAxisLabel.add(key.toMillis().fromMillis(DAY_MONTH))
        }

        for ((key, value) in historic.timeLine.deaths) {
            val x_points = key.toMillis().toFloat()
            val y_points: Float = value.toFloat()
            deaths.add(
                Entry(
                    x_points,
                    y_points,
                    null,
                    Triple(key.toMillis(), value, Constants.DEATHS)
                )
            )
        }

        for ((key, value) in historic.timeLine.recovered) {
            val x_points = key.toMillis().toFloat()
            val y_points: Float = value.toFloat()
            recovered.add(
                Entry(
                    x_points,
                    y_points,
                    null,
                    Triple(key.toMillis(), value, Constants.RECOVERED)
                )
            )
        }

        val casesDataSet = LineDataSet(cases, null).apply {
            lineWidth = 3.0f
            circleRadius = 4f
            color = getColor(requireContext(), R.color.blue)
            setCircleColor(getColor(requireContext(), R.color.blue))
            highLightColor = requireContext().getColor(R.color.blue)
            highlightLineWidth = 4f
            setDrawValues(false)
        }

        val deathsDataSet = LineDataSet(deaths, null).apply {
            lineWidth = 3.0f
            circleRadius = 4f
            color = getColor(requireContext(), R.color.red)
            setCircleColor(getColor(requireContext(), R.color.red))
            highLightColor = requireContext().getColor(R.color.red)
            highlightLineWidth = 4f
            setDrawValues(false)
        }

        val recoveredDataSet = LineDataSet(recovered, null).apply {
            lineWidth = 3f
            circleRadius = 4f
            color = getColor(requireContext(), R.color.green)
            setCircleColor(getColor(requireContext(), R.color.green))
            highLightColor = requireContext().getColor(R.color.green)
            highlightLineWidth = 4f
            setDrawValues(false)
        }
        val lineData = LineData(
            listOf(
                casesDataSet,
                deathsDataSet, recoveredDataSet
            )
        )

        mLineChart.apply {
            axisLeft.apply {
                valueFormatter = LargeValueFormatter()
            }
            xAxis.apply {
                setAvoidFirstLastClipping(true)
                spaceMax = 0.01f;
                spaceMin = 0.01f;
                setLabelCount(4 , false)
                isGranularityEnabled = true
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toLong().fromMillis(DAY_MONTH)
                    }
                }
            }
            setTouchEnabled(true)
            setDrawMarkers(true)
            markerView = MarkerView(
                requireContext(),
                R.layout.marker_layout
            )
            axisRight.setDrawLabels(false)
            setGridBackgroundColor(
                getColor(
                    requireContext(),
                    R.color.colorPrimaryDark
                )
            )
            description.isEnabled = false
            legend.isEnabled = false
            getPaint(LineChart.PAINT_DESCRIPTION).color =
                getColor(requireContext(), android.R.color.transparent)
            setDrawGridBackground(false)
            invalidate()
            animateX(750)
            data = lineData
            invalidate()
        }

    }


}
