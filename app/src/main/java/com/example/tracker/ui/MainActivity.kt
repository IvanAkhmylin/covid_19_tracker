package com.example.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tracker.R
import com.example.tracker.ui.fragments.CountriesStatisticFragment
import com.example.tracker.ui.fragments.OverallStatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var mBottomNavBar: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        mBottomNavBar = findViewById(R.id.nav_bar)
        supportFragmentManager.beginTransaction().replace(R.id.container,
            OverallStatisticFragment(), "").commitAllowingStateLoss()

        mBottomNavBar?.setOnNavigationItemSelectedListener { p0 ->
            if (p0.itemId == R.id.overall_statistic){
                supportFragmentManager.beginTransaction().replace(R.id.container,
                    OverallStatisticFragment(), "").commitAllowingStateLoss()
            }else {
                supportFragmentManager.beginTransaction().replace(R.id.container,
                    CountriesStatisticFragment(), "").commitAllowingStateLoss()
            }
            true
        }


    }
}
