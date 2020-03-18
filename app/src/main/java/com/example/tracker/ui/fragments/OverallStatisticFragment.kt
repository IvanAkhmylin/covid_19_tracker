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
import com.example.tracker.model.OverallStatisticModel
import com.example.tracker.viewmodel.OverallStatisticViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.math.RoundingMode
import kotlin.math.roundToInt


class OverallStatisticFragment : Fragment() {
    private var mChart: PieChart? = null
    private val mViewModel: OverallStatisticViewModel by viewModels()
    private var mConfirmed: TextView? = null
    private var mSick: TextView? = null
    private var mRecovered: TextView? = null
    private var mRecoveredPercent: TextView? = null
    private var mDeaths: TextView? = null
    private var mDeathsPercent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getOverallStatistic()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.overall_statistic_layout, container , false)
        init(v)
        return v
    }

    private fun init(v: View?) {
        mConfirmed = v?.findViewById(R.id.confirmed)
        mSick = v?.findViewById(R.id.sick)
        mRecovered = v?.findViewById(R.id.recovered)
        mRecoveredPercent = v?.findViewById(R.id.recovered_percent)
        mDeaths = v?.findViewById(R.id.deaths)
        mDeathsPercent = v?.findViewById(R.id.deaths_percent)

        mChart = v?.findViewById(R.id.pie_chart)
        initChart(0F, 0F, 0F)
        mViewModel.data.observe(
            viewLifecycleOwner,
            Observer {
                Toast.makeText(activity!!, it.deaths.toString(), Toast.LENGTH_LONG).show()
                initUI(it!!)
                initChart(
                    it.cases!!.toFloat(),
                    it.deaths!!.toFloat(),
                    it.recovered!!.toFloat()
                )
            })
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(it: OverallStatisticModel) {
        val confirmed = it.cases!! + it.recovered!! + it.deaths!!
        mConfirmed?.text = confirmed.toString()
        mSick?.text = (it.cases).toString()
        mRecovered?.text = (it.recovered).toString()
        mRecoveredPercent?.text = "${((it.recovered.toFloat() / confirmed) * 100).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)} %"
        mDeaths?.text = (it.deaths).toString()
        mDeathsPercent?.text = "${((it.deaths.toFloat() / confirmed) * 100).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)} %"
    }

    private fun initChart(cases: Float, deaths: Float, recovered: Float) {
        val data: ArrayList<PieEntry> = ArrayList()
        data.add(PieEntry(cases, "Sick"))
        data.add(PieEntry(deaths, "Deaths"))
        data.add(PieEntry(recovered, "Recovered"))

        val pieDataSet = PieDataSet(data, "")
        pieDataSet.setColors(
            arrayListOf(
                getColor(activity!!, R.color.blue),
                getColor(activity!!, R.color.red),
                getColor(activity!!, R.color.green)
            )
        )
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(17F)
        pieData.setValueTextColor(getColor(activity!!, R.color.white))
        mChart?.data = pieData
        mChart?.description?.isEnabled = false
        mChart?.legend?.isEnabled = false
        mChart?.holeRadius = 0f
        mChart?.transparentCircleRadius = 0f
        mChart?.setEntryLabelTextSize(20f)
        mChart?.animateXY(1000, 1000)
    }


}
