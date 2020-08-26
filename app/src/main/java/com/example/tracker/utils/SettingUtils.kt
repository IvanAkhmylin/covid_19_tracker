package com.example.tracker.utils

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.Context.UI_MODE_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.tracker.base.BaseActivity
import com.example.tracker.ui.MainActivity
import timber.log.Timber
import java.util.*


object SettingUtils {
    fun checkSettings(prefs: SharedPreferences, context: Context, targetSetting: String?) {
        if (targetSetting == null) {
            checkAppLang(prefs, context)
            checkAppTheme(prefs, context)

            if (!prefs.getBoolean("first_start", false)) {
                prefs.edit().putString("theme", "-1").apply()
                prefs.edit().putBoolean("first_start", true).apply()
            }

        } else {
            when (targetSetting) {
                "change_lang" -> checkAppLang(prefs, context)
                "theme" -> checkAppTheme(prefs, context)
            }
        }
        (context as AppCompatActivity).finish()
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    fun checkAppLang(prefs: SharedPreferences, activity: Context) {
        var lang = prefs.getString("change_lang", "")

        if (lang?.isEmpty()!!) {
            lang = Locale.getDefault().displayLanguage
            lang = lang.substring(0, 1).toUpperCase() + lang.substring(1)
            prefs.edit().putString("change_lang", lang).apply()
        }

        when (lang) {
            "English" -> changeAppLanguage("en", activity)
            "Русский" -> changeAppLanguage("ru", activity)
            "日本語" -> changeAppLanguage("ja", activity)
            else -> changeAppLanguage("en", activity)
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