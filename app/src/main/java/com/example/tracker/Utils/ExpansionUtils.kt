package com.example.tracker.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.tracker.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


object ExpansionUtils {

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
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}