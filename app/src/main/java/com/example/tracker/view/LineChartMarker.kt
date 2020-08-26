package com.example.tracker.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.example.tracker.Constants.Constants.CASES
import com.example.tracker.Constants.Constants.DAY_MONTH
import com.example.tracker.Constants.Constants.DEATHS
import com.example.tracker.Constants.Constants.RECOVERED
import com.example.tracker.R
import com.example.tracker.utils.ExpansionUtils.fromMillis
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_layout.view.*
import java.util.*


@SuppressLint("ViewConstructor")
class LineChartMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val data = e?.data as Triple<Long, Int, String>

        when (data.third) {
            CASES -> {
                tvDate.text = "${context.getString(R.string.date)}: ${data.first.fromMillis(
                    DAY_MONTH,
                    Locale(context.resources.getString(R.string.app_locale))
                )}"
                tvValue.text = "${context.getString(R.string.cases)}: +${data.second}"
                marker_card.strokeColor = context.getColor(R.color.blue)
            }
            DEATHS -> {
                tvDate.text = "${context.getString(R.string.date)}: ${data.first.fromMillis(
                    DAY_MONTH,
                    Locale(context.resources.getString(R.string.app_locale))
                )}"
                tvValue.text = "${context.getString(R.string.deaths)}: +${data.second}"
                marker_card.strokeColor = context.getColor(R.color.red)
            }
            RECOVERED -> {
                tvDate.text = "${context.getString(R.string.deaths)}: ${data.first.fromMillis(
                    DAY_MONTH,
                    Locale(context.resources.getString(R.string.app_locale))
                )}"
                tvValue.text = "${context.getString(R.string.recovered)}: +${data.second}"
                marker_card.strokeColor = context.getColor(R.color.green)
            }
        }
        super.refreshContent(e, highlight)

    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val min_offset = 600
        if (posX < min_offset)
            return MPPointF(15.0f, -height.toFloat().plus(15))
        if (metrics.widthPixels - posX < min_offset)
            return MPPointF(-width.toFloat().plus(15), -height.toFloat().plus(15))
        else if (metrics.widthPixels - posX < 0)
            return MPPointF(-width.toFloat().plus(15), -height.toFloat().plus(15))
        else
            return MPPointF(-(width / 2).toFloat().plus(15), -height.toFloat().plus(15))
    }
}