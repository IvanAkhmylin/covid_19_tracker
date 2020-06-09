package com.example.tracker.ui.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants.DAY
import com.example.tracker.Constants.Constants.MONTH
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.decimalFormatter
import com.example.tracker.Utils.ExpansionUtils.fromMillis
import com.example.tracker.Utils.ExpansionUtils.toMillis
import com.example.tracker.model.Historic

class CountriesDaysAdapter(val historic: Historic) : RecyclerView.Adapter<CountriesDaysAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.country_detail_bottom_sheet,parent, false))
        return v
    }
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun getItemCount(): Int = historic.timeLine.cases.size

    override fun onBindViewHolder(holder: CountriesDaysAdapter.ViewHolder, position: Int) {
        val mDay = holder.v.findViewById<TextView>(R.id.day)
        val mMonth = holder.v.findViewById<TextView>(R.id.month)
        val mCases = holder.v.findViewById<TextView>(R.id.cases)
        val mRecovered = holder.v.findViewById<TextView>(R.id.recovered)
        val mDeaths = holder.v.findViewById<TextView>(R.id.deaths)

        val keys = historic.timeLine.cases.keys.reversed().toTypedArray()
        val keyByPosition = keys[position]
        val day = keyByPosition.toMillis().fromMillis(DAY)
        val month = keyByPosition.toMillis().fromMillis(MONTH)
        val cases = historic.timeLine.cases[keyByPosition]
        val recovered = historic.timeLine.recovered[keyByPosition]
        val death= historic.timeLine.deaths[keyByPosition]

        mDay.text = day
        mMonth.text = month
        mCases.text = cases?.decimalFormatter()
        mRecovered.text = recovered?.decimalFormatter()
        mDeaths.text = death?.decimalFormatter()
    }
}