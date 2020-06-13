package com.example.tracker.ui.statistic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.Utils
import com.example.tracker.Utils.ExpansionUtils.decimalFormatter
import com.example.tracker.Utils.ExpansionUtils.setColorBefore
import com.example.tracker.Utils.ExpansionUtils.timestampToDate
import com.example.tracker.model.Statistic
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.loading_and_error_layout.*
import kotlinx.android.synthetic.main.statistic_layout.*
import kotlin.collections.ArrayList


class StatisticFragment : Fragment() {
    private var mChart: PieChart? = null
    private val mViewModel: StatisticViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.statistic_layout, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        mChart = v.findViewById(R.id.pie_chart)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getStatistic()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart(0F, 0F, 0F)
        initObservers()
    }

    private fun initObservers() {
        mViewModel.mNewsListStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    failure_container?.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    failure_container.visibility = View.GONE
                    progress.visibility = View.GONE
                    overall_container.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progress.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    overall_container.visibility = View.GONE
                }

            }

        })

        mViewModel.mStatistic.observe(viewLifecycleOwner, Observer {
            overall_container?.visibility = View.VISIBLE
            initStatistic(it!!)
            initChart(
                it.cases!!.toFloat(),
                it.deaths!!.toFloat(),
                it.recovered!!.toFloat()
            )
        })


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initStatistic(it: Statistic) {
        updated.text = it.updated.timestampToDate()

        cases.text = it.cases?.decimalFormatter()
        deaths.text = it.deaths?.decimalFormatter()
        recovered.text = it.recovered?.decimalFormatter()

        cases_today.text = "+${it.todayCases?.decimalFormatter()} today"
        cases_today.setColorBefore("today")

        deaths_today.text = "+${it.todayDeaths?.decimalFormatter()} today"
        deaths_today.setColorBefore("today")

        recovered_today.text = "+${it.todayRecovered?.decimalFormatter()} today"
        recovered_today.setColorBefore("today")

        active.text = it.active?.decimalFormatter()
        tests.text = it.tests?.decimalFormatter()
        critical.text = it.critical?.decimalFormatter()
        cases_per_million.text = it.casesPerOneMillion?.toString()
        deaths_per_million.text = it.deathsPerOneMillion?.toString()
        tests_per_million.text = it.testsPerOneMillion?.toString()
        affected_country.text = it.affectedCountries?.decimalFormatter()


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
