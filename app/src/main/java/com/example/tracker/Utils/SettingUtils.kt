package com.example.tracker.Utils

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.Context.UI_MODE_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.tracker.SplashScreen
import com.example.tracker.ui.MainActivity
import java.util.*


object SettingUtils {
    fun checkSettings(prefs: SharedPreferences, context: Context, targetSetting: String?) {
        if (targetSetting == null) {
            checkAppLang(prefs, context)
            checkAppTheme(prefs, context)

            if(!prefs.getBoolean("first_start" , false)){
                prefs.edit().putString("theme" , "-1").apply()
                prefs.edit().putString("change_lang" , "English").apply()
                prefs.edit().putBoolean("first_start" , true).apply()
            }

        } else {
            when (targetSetting) {
                "change_lang" -> checkAppLang(prefs, context)
                "theme" -> checkAppTheme(prefs, context)
            }
        }
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }

    fun checkAppLang(prefs: SharedPreferences, activity: Context) {
        val lang = prefs.getString("change_lang", "English")
        when (lang) {
            "English" -> changeAppLanguage("en", activity)
            "Русский" -> changeAppLanguage("ru", activity)
            "日本" -> changeAppLanguage("ja", activity)
        }
    }

    private fun changeAppLanguage(locale: String, context: Context) {
        val res: Resources = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(locale)
        res.updateConfiguration(conf, dm)
    }

    fun checkAppTheme(prefs: SharedPreferences, context: Context) {
        val theme = prefs.getString("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString())
        val uiManager = context.getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiManager.nightMode = theme?.toInt()!!
        AppCompatDelegate.setDefaultNightMode(theme.toInt())
    }
}