package com.example.tracker.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.timestampToDate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


object ExpansionUtils {
    @SuppressLint("SimpleDateFormat")
    fun Long.timestampToDate() : String{
        val dateFormat = SimpleDateFormat("dd MMMM")
        val timeFormat = SimpleDateFormat("k:mm")
        val netDate = Date(this)
        val date = dateFormat.format(netDate)
        val time = timeFormat.format(netDate)

        return "Data Updated: $date at $time"
    }
    fun ArrayList<String>.getAreaCount(): ArrayList<String>? {
        val label: ArrayList<String> = ArrayList()
        for (i in 0 until this.size) label.add(this.get(i))
        return label
    }
    fun String.toMillis(): Long{
        val df = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
        return df.parse(this).time
    }

    fun Long.fromMillis(format: String) : String{
        val dateFormat = java.text.SimpleDateFormat(format)
        val netDate = Date(this.toLong())
        return dateFormat.format(netDate)
    }

    @SuppressLint("ResourceType")
    fun TextView.setColorBefore(word: String) {
        val text = SpannableString(this.text.toString())

        val foregroundSpan = ForegroundColorSpan(Color.parseColor(resources.getString(
            R.color.colorAccent
        )))
        text.setSpan(
            foregroundSpan,
            0,
            text.length - word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.text = text
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun Int.decimalFormatter(): String {
        val format = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
        format.applyPattern("#,###,###")
        return format.format(this).replace(("\\s+").toRegex(), ",")
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}