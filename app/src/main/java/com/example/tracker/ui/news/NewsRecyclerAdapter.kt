package com.example.tracker.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.model.News
import com.facebook.drawee.view.SimpleDraweeView

class NewsRecyclerAdapter(
    val data: ArrayList<News>,
    val onClick: (News) -> Unit,
    val onLongClick: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))
        return v
    }
    override fun getItemCount(): Int {
        return data.size - 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder.itemView.findViewById<CardView>(R.id.item)
        val title = holder.itemView.findViewById<TextView>(R.id.title)
        val resource = holder.itemView.findViewById<TextView>(R.id.resource)
        val image = holder.itemView.findViewById<SimpleDraweeView>(R.id.news_image)

        image.setImageURI(data[position].image)
        title.text = data[position].title
        resource.text = "${data[position].resource} • ${data[position].time}"
        item.setOnClickListener {
            onClick(data[position])
        }

        item.setOnLongClickListener {
            onLongClick(data[position].link)
            true
        }
    }

}