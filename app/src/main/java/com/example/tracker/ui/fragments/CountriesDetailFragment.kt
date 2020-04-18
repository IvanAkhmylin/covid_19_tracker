package com.example.tracker.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.facebook.drawee.view.SimpleDraweeView

class CountriesDetailFragment(dataModel: CountriesStatisticModel?) : Fragment() {
    private var data = dataModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.country_info , container,  false)
        init(v)
        return v
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun init(v: View) {
        data?.countryInfo?.flag?.let {
            v.findViewById<SimpleDraweeView>(R.id.country_flag).setImageURI(data?.countryInfo?.flag)
        }
        data?.country?.let {
            v.findViewById<TextView>(R.id.country_name).text = data?.country
        }
        data?.countryInfo?.iso2?.let {
            v.findViewById<TextView>(R.id.country_iso).text = "(${data?.countryInfo?.iso2}, ${data?.countryInfo?.iso3})"
        }
        data?.cases?.let {
            v.findViewById<TextView>(R.id.cases).text = data?.cases.toString()
        }
        data?.active?.let {
            v.findViewById<TextView>(R.id.active).text = data?.active.toString()
        }
        data?.critical?.let {
            v.findViewById<TextView>(R.id.critical).text = data?.critical.toString()
        }
        data?.todayCases?.let {
            v.findViewById<TextView>(R.id.today_cases).text = data?.todayCases.toString()
        }
        data?.deaths?.let {
            v.findViewById<TextView>(R.id.deaths).text = data?.deaths.toString()
        }
        data?.todayDeaths?.let {
            v.findViewById<TextView>(R.id.today_deaths).text = data?.todayDeaths.toString()
        }
        data?.recovered?.let {
            v.findViewById<TextView>(R.id.recovered).text = data?.recovered.toString()
        }

        data?.tests?.let {
            v.findViewById<TextView>(R.id.tests).text = data?.tests.toString()
        }
        data?.casesPerOneMillion?.let {
            v.findViewById<TextView>(R.id.cases_per_million).text = data?.casesPerOneMillion.toString()
        }
        data?.deathsPerOneMillion?.let {
            v.findViewById<TextView>(R.id.deaths_per_million).text = data?.deathsPerOneMillion.toString()
        }

    }

}