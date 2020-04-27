package com.example.tracker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tracker.App.Companion.hideKeyboard
import com.example.tracker.App.Companion.showKeyboard
import com.example.tracker.Constants
import com.example.tracker.R
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.details.CountriesDetailFragment
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.fragment_search.*
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment() {
    private lateinit var mViewModel: SearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        init(v)
        return v
    }

    @SuppressLint("SetTextI18n")
    private fun init(v: View) {
        mViewModel = ViewModelProvider(requireActivity() as MainActivity).get(SearchViewModel::class.java)

        mViewModel.mFailureMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.mShowProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress.visibility = View.VISIBLE
                search_container.visibility = View.GONE
            } else {
                progress.visibility = View.GONE
            }
        })

        mViewModel.mCountry.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a")
                val netDate = Date(data.updated)

                search_container.visibility = View.VISIBLE
                country_flag.setImageURI(data.countryInfo.flag)
                country_name.text = data.country

                v.findViewById<TextView>(R.id.last_update).text =
                    "Updated: ${dateFormat.format(netDate)}"
                v.findViewById<TextView>(R.id.country_iso).text =
                    "Country Code: ${data?.countryInfo?.iso2}, ${data?.countryInfo?.iso3}"
                v.findViewById<TextView>(R.id.continent).text = "Continent: ${data?.continent}"
                v.findViewById<Button>(R.id.show_details).setOnClickListener {

                    (requireActivity() as MainActivity).swapFragment(
                        R.id.container,
                        CountriesDetailFragment(data), Constants.fragmentDetailSearch, Constants.ANIM_SLIDE_LEFT
                    )
                }

                v.findViewById<Button>(R.id.show_on_map).setOnClickListener {
                    Toast.makeText(requireContext(), "Map", Toast.LENGTH_SHORT).show()
                }
            } else {
                search_container.visibility = View.GONE
            }
        })

    }



}


