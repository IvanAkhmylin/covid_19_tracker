package com.example.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.tracker.App
import com.example.tracker.R
import com.example.tracker.ui.fragments.MapStatisticFragment
import com.example.tracker.ui.fragments.OverallStatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

open class MainActivity : AppCompatActivity() {
    // TODO need move NavDrawer logic to MainActivity from MapStatisticFragment (My mistake...i think )
    val mMapFragment: MapStatisticFragment = MapStatisticFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0){
                finish()
            }
        }

        swapFragment(R.id.container,mMapFragment)
//        mBottomNavBar?.apply {
//            this.setOnNavigationItemSelectedListener { p0 ->
//                if (p0.itemId == R.id.overall_statistic){
//                    supportFragmentManager.beginTransaction().replace(R.id.container,
//                        mOverallFragment, "").commit()
//                }else{
//                    supportFragmentManager.beginTransaction().replace(R.id.container,
//                        mMapFragment, "").commit()
//                }
//                true
//            }
//
//            this.selectedItemId = R.id.overall_statistic
//        }
    }


    fun swapFragment(res: Int, fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(res,fragment)
        transaction.commit()
    }


}
