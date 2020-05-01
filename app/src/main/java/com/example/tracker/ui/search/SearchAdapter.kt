package com.example.tracker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.model.Country
import com.facebook.drawee.view.SimpleDraweeView

class SearchAdapter(private val items: List<Country>, var onClick : (Country) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout,parent, false))
        return v
    }
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val countryName = holder.v.findViewById<TextView>(R.id.country_name)
        val countryIso = holder.v.findViewById<TextView>(R.id.country_iso)
        val flag = holder.v.findViewById<SimpleDraweeView>(R.id.country_flag)
        val button = holder.v.findViewById<ImageButton>(R.id.icon_show)

        flag.setImageURI(items[position].countryInfo.flag)
        countryName.text = items[position].country
        countryIso.text = "${items[position].countryInfo.iso2}, ${items[position].countryInfo.iso3}"
        button.setOnClickListener{
            onClick(items[position])
        }
    }



}