package com.example.tracker.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants.STATE_LOADING
import com.example.tracker.Constants.Constants.STATE_NORMAL
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.data.local.entity.News
import com.facebook.drawee.view.SimpleDraweeView

class NewsRecyclerAdapter(
    val onClick: (News) -> Unit,
    val onLongClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = ArrayList<News>()
    var animatedPosition = -1
    var loading = ""

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == STATE_NORMAL) {
            val v = ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
            )
            return v
        } else {
            val v = ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_bottom_loading_layout, parent, false)
            )
            return v
        }
    }


    override fun getItemCount(): Int {
        if (loading == Status.LOADING) {
            return data.size + 1
        } else {
            return data.size
        }
    }

    fun updateRecyclerAdapter(status: String, news: ArrayList<News>) {
        if (news.size == 1) {
            animatedPosition = -1
        }

        data.clear()

        loading = status
        data.addAll(news)

        notifyItemChanged(data.size)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < data.size) {
            return STATE_NORMAL
        } else {
            return STATE_LOADING
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == STATE_NORMAL) {
            val context = holder.itemView.context
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

            setRecyclerViewAnimation(context, holder.itemView, position)
        }
    }

    private fun setRecyclerViewAnimation(context: Context, itemView: View, position: Int) {
        if (position > animatedPosition) {
            AnimationUtils.loadAnimation(context, R.anim.fall_down_animation).apply {
                itemView.animation = this
                start()
            }
            animatedPosition = position
        }
    }

    fun clearAdapterData() {
        data.clear()
        loading = ""
        notifyDataSetChanged()
    }


}