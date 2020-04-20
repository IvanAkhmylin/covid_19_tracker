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
import kotlinx.android.synthetic.main.overall_statistic_layout.*
import java.text.SimpleDateFormat
import java.util.*

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

        data.apply {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a")
            val netDate = Date(this!!.updated)

            v.findViewById<SimpleDraweeView>(R.id.country_flag).setImageURI(data?.countryInfo?.flag)
            v.findViewById<TextView>(R.id.last_update).text = "Updated: ${dateFormat.format(netDate)}"
            v.findViewById<TextView>(R.id.country_name).text = "Country: ${data?.country}"
            v.findViewById<TextView>(R.id.country_iso).text = "Country Code: ${data?.countryInfo?.iso2}, ${data?.countryInfo?.iso3}"
            v.findViewById<TextView>(R.id.continent).text = "Continent ${data?.continent}"

            v.findViewById<TextView>(R.id.cases).text = data?.cases.toString()
            v.findViewById<TextView>(R.id.active).text = data?.active.toString()
            v.findViewById<TextView>(R.id.critical).text = data?.critical.toString()
            v.findViewById<TextView>(R.id.today_cases).text = data?.todayCases.toString()
            v.findViewById<TextView>(R.id.deaths).text = data?.deaths.toString()
            v.findViewById<TextView>(R.id.today_deaths).text = data?.todayDeaths.toString()
            v.findViewById<TextView>(R.id.recovered).text = data?.recovered.toString()
            v.findViewById<TextView>(R.id.tests).text = data?.tests.toString()
            v.findViewById<TextView>(R.id.cases_per_million).text = data?.casesPerOneMillion.toString()
            v.findViewById<TextView>(R.id.deaths_per_million).text = data?.deathsPerOneMillion.toString()
            v.findViewById<TextView>(R.id.tests_per_million).text = data?.testsPerOneMillion.toString()

        }




    }

}