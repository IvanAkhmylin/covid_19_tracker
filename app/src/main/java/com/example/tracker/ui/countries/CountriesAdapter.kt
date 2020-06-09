package com.example.tracker.ui.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.decimalFormatter
import com.example.tracker.model.Country
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.card.MaterialCardView

class CountriesAdapter(private val items: List<Country>, var onClick : (Country) -> Unit) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout,parent, false))
        return v
    }
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CountriesAdapter.ViewHolder, position: Int) {
        val countryName = holder.v.findViewById<TextView>(R.id.country_name)
        val cases = holder.v.findViewById<TextView>(R.id.cases)
        val updated = holder.v.findViewById<TextView>(R.id.updated)
        val recovered = holder.v.findViewById<TextView>(R.id.recovered)
        val deaths = holder.v.findViewById<TextView>(R.id.deaths)
        val flag = holder.v.findViewById<SimpleDraweeView>(R.id.flag)
        val card = holder.v.findViewById<MaterialCardView>(R.id.item)

        flag.setImageURI(items[position].countryInfo.flag)
        countryName.text = items[position].country
        cases.text = items[position].cases?.decimalFormatter()
        recovered.text = items[position].recovered?.decimalFormatter()
        deaths.text = items[position].deaths?.decimalFormatter()
        card.setOnClickListener{
            onClick(items[position])
        }
    }



}