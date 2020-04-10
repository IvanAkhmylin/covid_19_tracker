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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tracker.R
import com.example.tracker.model.StatisticModel
import com.example.tracker.viewmodel.StatisticViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.math.RoundingMode


class OverallFragment : Fragment() {
    private var mChart: PieChart? = null
    private val mViewModel: StatisticViewModel by viewModels()
    private var mConfirmed: TextView? = null
    private var mCases: TextView? = null
    private var mRecovered: TextView? = null
    private var mRecoveredPercent: TextView? = null
    private var mDeaths: TextView? = null
    private var mDeathsPercent: TextView? = null
    private var mRefresher: SwipeRefreshLayout? = null


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
                mRefresher?.isRefreshing = false
                initStatistic(it!!)
                initChart(
                    it.cases!!.toFloat(),
                    it.deaths!!.toFloat(),
                    it.recovered!!.toFloat()
                )
            })
        mViewModel.mFailureMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity!!, it, Toast.LENGTH_LONG).show()
            mRefresher?.isRefreshing = false
        })

    }

    private fun init(v: View?) {
        mConfirmed = v?.findViewById(R.id.confirmed)
        mCases = v?.findViewById(R.id.cases)
        mRecovered = v?.findViewById(R.id.recovered)
        mRecoveredPercent = v?.findViewById(R.id.recovered_percent)
        mDeaths = v?.findViewById(R.id.deaths)
        mDeathsPercent = v?.findViewById(R.id.deaths_percent)
        mChart = v?.findViewById(R.id.pie_chart)
        mRefresher = v?.findViewById(R.id.refresher)
        mRefresher?.setOnRefreshListener { mViewModel.getStatistic() }

    }

    @SuppressLint("SetTextI18n")
    private fun initStatistic(it: StatisticModel) {
        val confirmed = it.cases!! + it.recovered!! + it.deaths!!
        mConfirmed?.text = confirmed.toString()
        mCases?.text = (it.cases).toString()
        mRecovered?.text = (it.recovered).toString()
        mRecoveredPercent?.text = "${((it.recovered.toFloat() / confirmed) * 100).toBigDecimal()
            .setScale(2, RoundingMode.HALF_EVEN)} %"
        mDeaths?.text = (it.deaths).toString()
        mDeathsPercent?.text = "${((it.deaths.toFloat() / confirmed) * 100).toBigDecimal()
            .setScale(2, RoundingMode.HALF_EVEN)} %"
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
                    getColor(activity!!, R.color.blue),
                    getColor(activity!!, R.color.red),
                    getColor(activity!!, R.color.green)
                )
            )
        }

        val pieData = PieData(pieDataSet).apply {
            this.setValueTextSize(17F)
            this.setValueTextColor(getColor(activity!!, R.color.white))
        }

        mChart?.apply {
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
