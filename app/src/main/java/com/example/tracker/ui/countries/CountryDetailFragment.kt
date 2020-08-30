package com.example.tracker.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.utils.Utils
import com.example.tracker.data.local.entity.Historic
import com.example.tracker.databinding.FragmentCountriesBinding
import com.example.tracker.databinding.FragmentDetailCountryBinding
import com.example.tracker.databinding.FragmentNewsBinding
import com.example.tracker.ui.MainActivity
import com.github.mikephil.charting.charts.LineChart
import kotlinx.android.synthetic.main.activity_main.*


class CountryDetailFragment : BaseFragment() {
    private var _binding: FragmentDetailCountryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailCountryBinding.inflate(inflater, container, false)
        val v = binding!!.root
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val countryName =  it.getString(Constants.COUNTRIES_NAME)
            val history =  it.get(Constants.HISTORIC_OF_COUNTRIES) as Historic

            (requireContext() as MainActivity).toolbar.title = countryName

            history.let {
                Utils.initializeLineChart(binding?.lineChart!! , null, requireContext(), it.timeLine!!) // init LineChart
                binding?.statisticDays?.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                binding?.statisticDays?.adapter = CountriesDaysAdapter(it.timeLine!!)
                binding?.statisticDays?.adapter!!.notifyDataSetChanged()
            }

        }
    }

}
