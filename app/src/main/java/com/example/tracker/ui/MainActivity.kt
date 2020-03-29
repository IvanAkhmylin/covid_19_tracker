package com.example.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tracker.R
import com.example.tracker.ui.fragments.MapStatisticFragment
import com.example.tracker.ui.fragments.OverallStatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var mBottomNavBar: BottomNavigationView? = null
    private val mOverallFragment: OverallStatisticFragment = OverallStatisticFragment()
    private val mMapFragment: MapStatisticFragment = MapStatisticFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
//        mBottomNavBar = findViewById(R.id.nav_bar)
        supportFragmentManager.beginTransaction().replace(R.id.container,
            mMapFragment, "").commit()
//        mBottomNavBar?.apply {
//            this.setOnNavigationItemSelectedListener { p0 ->
//                if (p0.itemId == R.id.overall_statistic){
//                    supportFragmentManager.beginTransaction().replace(R.id.container,
//                        mOverallFragment, "").commit()
//                }else {
//                    supportFragmentManager.beginTransaction().replace(R.id.container,
//                        mMapFragment, "").commit()
//                }
//                true
//            }
//
//            this.selectedItemId = R.id.overall_statistic
//        }


    }
}
