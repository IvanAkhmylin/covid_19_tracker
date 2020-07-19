package com.example.tracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceManager
import com.example.tracker.Utils.SettingUtils
import java.util.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        SettingUtils.checkSettings(prefs , this, null)
    }

}