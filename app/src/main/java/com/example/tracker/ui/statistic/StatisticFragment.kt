package com.example.tracker.ui.statistic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.R
import com.example.tracker.model.Statistic
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.statistic_layout.*
import java.text.SimpleDateFormat
import java.util.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart(0F, 0F, 0F)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.waiting_dialog)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

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
            if (it){
                overall_container?.visibility = View.GONE
                dialog.show()
            }else{
                overall_container?.visibility = View.VISIBLE
                dialog.dismiss()
            }
        })
    }

    private fun init(v: View?) {
        mChart = v?.findViewById(R.id.pie_chart)

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initStatistic(it: Statistic) {
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
