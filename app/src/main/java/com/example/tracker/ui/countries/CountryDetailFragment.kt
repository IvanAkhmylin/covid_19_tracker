package com.example.tracker.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.R
import com.example.tracker.Utils.Utils
import com.example.tracker.model.Historic
import com.example.tracker.ui.MainActivity
import com.github.mikephil.charting.charts.LineChart
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail_country.*


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
            val countryName =  it.getString(Constants.COUNTRIES_NAME)
            val history =  it.get(Constants.HISTORIC_OF_COUNTRIES) as Historic

            (requireContext() as MainActivity).toolbar.title = countryName

            history.let {
                Utils.initializeLineChart(mLineChart , null, requireContext(), it.timeLine) // init LineChart
                mRecyclerView.adapter = CountriesDaysAdapter(it.timeLine)
                mRecyclerView.adapter!!.notifyDataSetChanged()
            }

        }
    }

}
