package com.example.tracker.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.example.tracker.Constants.Constants
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.fromMillis
import com.example.tracker.Utils.ExpansionUtils.toMillis
import com.example.tracker.model.TimeLine
import com.example.tracker.view.LineChartMarker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*


object Utils {


    fun createDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
//        builder.setView(R.layout.waiting_dialog)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }


    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context!!, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun shareNews(it: String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, it)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.choose)))
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initializeLineChart(
        mLineChart: LineChart,
        mScrollView: NestedScrollView?,
        context: Context,
        timeline: TimeLine
    ) {
        val xAxisLabel = ArrayList<String>(timeline.cases.size)
        val cases = ArrayList<Entry>()
        val deaths = ArrayList<Entry>()
        val recovered = ArrayList<Entry>()

        val keys = timeline.cases.keys.toTypedArray()

        for ((index, key) in keys.withIndex()) {
            val previousKey = keys.getOrNull(index - 1)


            if (previousKey != null) {
                val increasedValue = timeline.cases.getOrDefault(key, 0)
                    .minus(timeline.cases.getOrDefault(previousKey, 0))

                val data = Triple<Long, Int, String>(
                    key.toMillis(),
                    increasedValue,
                    Constants.CASES
                )

                val x_points = key.toMillis().toFloat()
                val y_points: Float = increasedValue.toFloat()
                cases.add(
                    Entry(
                        x_points,
                        y_points,
                        null,
                        data
                    )
                )
                xAxisLabel.add(key.toMillis().fromMillis(Constants.DAY_MONTH,  Locale(context.resources.getString(R.string.app_locale))))
            }
        }

        for ((index, key) in keys.withIndex()) {
            val previousKey = keys.getOrNull(index - 1)
            if (previousKey != null) {
                val increasedValue = timeline.recovered.getOrDefault(key, 0)
                    .minus(timeline.recovered.getOrDefault(previousKey, 0))


                val data = Triple<Long, Int, String>(
                    key.toMillis(),
                    increasedValue,
                    Constants.RECOVERED
                )

                val x_points = key.toMillis().toFloat()
                val y_points: Float = increasedValue.toFloat()
                recovered.add(
                    Entry(
                        x_points,
                        y_points,
                        null,
                        data
                    )
                )
                xAxisLabel.add(key.toMillis().fromMillis(Constants.DAY_MONTH , Locale(context.resources.getString(R.string.app_locale))))
            }
        }

        for ((index, key) in keys.withIndex()) {
            val previousKey = keys.getOrNull(index - 1)
            if (previousKey != null) {
                val increasedValue = timeline.deaths.getOrDefault(key, 0)
                    .minus(timeline.deaths.getOrDefault(previousKey, 0))

                val data = Triple<Long, Int, String>(
                    key.toMillis(),
                    increasedValue,
                    Constants.DEATHS
                )

                val x_points = key.toMillis().toFloat()
                val y_points: Float = increasedValue.toFloat()
                deaths.add(
                    Entry(
                        x_points,
                        y_points,
                        null,
                        data
                    )
                )
                xAxisLabel.add(key.toMillis().fromMillis(Constants.DAY_MONTH,  Locale(context.resources.getString(R.string.app_locale))))
            }
        }



        val casesDataSet = LineDataSet(cases, null).apply {
            lineWidth = 3.0f
            circleRadius = 1f
            color = ContextCompat.getColor(context, R.color.blue)
            setCircleColor(ContextCompat.getColor(context, R.color.blue))
            highLightColor = context.getColor(R.color.blue)
            highlightLineWidth = 2f
            setDrawValues(false)
        }

        val deathsDataSet = LineDataSet(deaths, null).apply {
            lineWidth = 3.0f
            circleRadius = 1f
            color = ContextCompat.getColor(context, R.color.red)
            setCircleColor(ContextCompat.getColor(context, R.color.red))
            highLightColor = context.getColor(R.color.red)
            highlightLineWidth = 2f
            setDrawValues(false)
        }

        val recoveredDataSet = LineDataSet(recovered, null).apply {
            lineWidth = 3f
            circleRadius = 1f
            color = ContextCompat.getColor(context, R.color.green)
            setCircleColor(ContextCompat.getColor(context, R.color.green))
            highLightColor = context.getColor(R.color.green)
            highlightLineWidth = 2f
            setDrawValues(false)
        }
        val lineData = LineData(
            listOf(
                casesDataSet,
                deathsDataSet, recoveredDataSet
            )
        )

        mLineChart.apply {
            axisLeft.apply {
                textColor = context.getColor(R.color.primary_text)
                valueFormatter = LargeValueFormatter()
            }
            xAxis.apply {
                textColor = context.getColor(R.color.primary_text)
                setAvoidFirstLastClipping(true)
                spaceMax = 0.01f
                spaceMin = 0.01f
                setLabelCount(4, false)
                isGranularityEnabled = true
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toLong().fromMillis(Constants.DAY_MONTH ,  Locale(context.resources.getString(R.string.app_locale)))
                    }
                }
            }
            setTouchEnabled(true)
            setDrawMarkers(true)
            markerView = LineChartMarker(
                context,
                R.layout.marker_layout
            )
            axisRight.setDrawLabels(false)
            setGridBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimaryDark
                )
            )
            description.isEnabled = false
            legend.isEnabled = false
            getPaint(LineChart.PAINT_DESCRIPTION).color =
                ContextCompat.getColor(context, android.R.color.transparent)
            setDrawGridBackground(false)
            invalidate()
            animateX(750)
            data = lineData
            invalidate()
            visibility = View.VISIBLE
        }

        mScrollView?.let {
            mLineChart.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mScrollView.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        mScrollView.requestDisallowInterceptTouchEvent(false)
                    }
                }
                false
            }
        }

    }


     fun initializePieChart(
        mChart: PieChart,
        cases: Float,
        deaths: Float,
        recovered: Float,
        context: Context
    ) {
        val data: ArrayList<PieEntry> = ArrayList()
        data.apply {
            this.add(PieEntry(cases, context.getString(R.string.cases)))
            this.add(PieEntry(deaths, context.getString(R.string.deaths)))
            this.add(PieEntry(recovered, context.getString(R.string.recovered)))
        }

        val pieDataSet = PieDataSet(data, "").apply {
            this.setColors(
                arrayListOf(
                    ContextCompat.getColor(context, R.color.blue),
                    ContextCompat.getColor(context, R.color.red),
                    ContextCompat.getColor(context, R.color.green)
                )
            )
        }

        val pieData = PieData(pieDataSet).apply {
            this.setValueTextSize(17F)
            this.setValueTextColor(ContextCompat.getColor(context, R.color.white))
        }

        mChart.apply {
            this.isHighlightPerTapEnabled = false
            this.data = pieData
            this.description.isEnabled = false
            this.legend?.isEnabled = false
            this.holeRadius = 0f
            this.transparentCircleRadius = 0f
            this.setEntryLabelTextSize(20f)
            this.animateXY(0, 0)
        }

    }


}