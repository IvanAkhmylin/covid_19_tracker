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
import com.example.tracker.R
import com.example.tracker.Utils.Utils
import com.example.tracker.Utils.ExpansionUtils.decimalFormatter
import com.example.tracker.Utils.ExpansionUtils.setColorBefore
import com.example.tracker.model.Statistic
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart(0F, 0F, 0F)
        initObservers()
    }

    private fun initObservers() {
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
        mViewModel.mShowProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                overall_container?.visibility = View.GONE
                progress?.visibility = View.VISIBLE
            } else {
                overall_container?.visibility = View.VISIBLE
                progress?.visibility = View.GONE
            }
        })
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initStatistic(it: Statistic) {
        updated.text = Utils.timestampToDate(it.updated)

        cases.text = it.cases?.decimalFormatter()
        deaths.text = it.deaths?.decimalFormatter()

        cases_today.text = "+${it.todayCases?.decimalFormatter()} today"
        cases_today.setColorBefore("today")

        deaths_today.text = "+${it.todayDeaths?.decimalFormatter()} today"
        deaths_today.setColorBefore("today")

        recovered.text = it.recovered?.decimalFormatter()
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
