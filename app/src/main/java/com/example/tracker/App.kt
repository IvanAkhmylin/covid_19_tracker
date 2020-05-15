package com.example.tracker

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.facebook.drawee.backends.pipeline.Fresco


class App : Application(){
    override fun onCreate() {
        Fresco.initialize(this)
        super.onCreate()
    }

}