package com.example.tracker

import android.app.Application
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.example.tracker.Utils.SettingUtils
import com.facebook.drawee.backends.pipeline.Fresco
import java.util.*

class App : Application() {
    override fun onCreate() {
        Fresco.initialize(this)
        super.onCreate()
    }

}