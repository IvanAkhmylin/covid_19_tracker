package com.example.tracker.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.tracker.R
import com.example.tracker.Utils.SettingUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mNavController by lazy {
        Navigation.findNavController(this, R.id.nav_host)
    }

    private val sharedPreferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            Log.d("TAG" , "$sharedPreferences --- $key")
            SettingUtils.checkSettings(sharedPreferences, this, key)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    private fun init() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)

        setSupportActionBar(findViewById(R.id.toolbar))
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.news,
                R.id.statistics,
                R.id.countries,
                R.id.map,
                R.id.settings
            ), drawer_layout
        )
        setupDrawerLayout()
    }

    private fun setupDrawerLayout() {
        NavigationUI.setupWithNavController(nav_view, mNavController)
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (drawer_layout?.isDrawerOpen(GravityCompat.START)!!) {
            drawer_layout?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}