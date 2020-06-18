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
import com.example.tracker.Utils.Utils
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
        mViewModel =
            ViewModelProvider(requireActivity() as MainActivity).get(CountriesViewModel::class.java)
        mRecyclerView = v.findViewById(R.id.statistic_days)
        mRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        mLineChart = v.findViewById(R.id.line_chart)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            (requireContext() as MainActivity).toolbar.title =
                it.getString(Constants.COUNTRIES_TITLE)
            val historicData = it.get(Constants.HISTORIC_OF_COUNTRIES) as Historic

            historicData.let {
                Utils.initializeLineChart(mLineChart , null, requireContext(), it.timeLine) // init LineChart
                mRecyclerView.adapter = CountriesDaysAdapter(it.timeLine)
                mRecyclerView.adapter!!.notifyDataSetChanged()
            }

        }
    }

}
