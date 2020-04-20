package com.example.tracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.R
import com.example.tracker.model.StatisticModel
import com.example.tracker.viewmodel.StatisticViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.overall_statistic_layout.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OverallFragment : Fragment() {
    private var mChart: PieChart? = null
    private val mViewModel: StatisticViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.overall_statistic_layout, container, false)
        init(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart(0F, 0F, 0F)
        mViewModel.mStatistic.observe(
            viewLifecycleOwner,
            Observer {
                initStatistic(it!!)
                initChart(
                    it.cases!!.toFloat(),
                    it.deaths!!.toFloat(),
                    it.recovered!!.toFloat()
                )
            })
        mViewModel.mFailureMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
        })

    }

    private fun init(v: View?) {

        mChart = v?.findViewById(R.id.pie_chart)


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initStatistic(it: StatisticModel) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy")
        val timeFormat = SimpleDateFormat("hh:mm:ss a")
        val netDate = Date(it.updated)

        date.text = dateFormat.format(netDate)
        time.text = timeFormat.format(netDate)

        cases.text = it.cases.toString()
        today_cases.text = it.todayCases.toString()
        deaths.text = it.deaths.toString()
        today_deaths.text = it.todayDeaths.toString()
        recovered.text = it.recovered.toString()
        active.text = it.active.toString()
        tests.text = it.tests.toString()
        critical.text = it.critical.toString()
        cases_per_million.text = it.casesPerOneMillion.toString()
        deaths_per_million.text = it.deathsPerOneMillion.toString()
        tests_per_million.text = it.testsPerOneMillion.toString()
        affected_country.text = it.affectedCountries.toString()



    }

    private fun initChart(cases: Float, deaths: Float, recovered: Float) {
        val data: ArrayList<PieEntry> = ArrayList()
        data.apply {
            this.add(PieEntry(cases, "Cases"))
            this.add(PieEntry(deaths, "Deaths"))
            this.add(PieEntry(recovered, "Recovered"))
        }

        val pieDataSet = PieDataSet(data, "").apply {
            this.setColors(
                arrayListOf(
                    getColor(requireActivity(), R.color.blue),
                    getColor(requireActivity(), R.color.red),
                    getColor(requireActivity(), R.color.green)
                )
            )
        }

        val pieData = PieData(pieDataSet).apply {
            this.setValueTextSize(17F)
            this.setValueTextColor(getColor(requireActivity(), R.color.white))
        }

        mChart?.apply {
            this.isHighlightPerTapEnabled = false
            this.data = pieData
            this.description.isEnabled = false
            this.legend?.isEnabled = false
            this.holeRadius = 0f
            this.transparentCircleRadius = 0f
            this.setEntryLabelTextSize(20f)
            this.animateXY(0, 0)
        }

    }


}
