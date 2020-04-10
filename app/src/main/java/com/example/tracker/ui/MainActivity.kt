package com.example.tracker.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.tracker.R
import com.example.tracker.ui.fragments.MapStatisticFragment
import com.example.tracker.ui.fragments.OverallFragment
import com.example.tracker.ui.fragments.SearchFragment
import com.example.tracker.view.DrawerButton
import com.google.android.material.navigation.NavigationView

open class MainActivity : AppCompatActivity() {
    private val mMapFragment: MapStatisticFragment = MapStatisticFragment()
    private val mOverallFragment: OverallFragment = OverallFragment()
    private var mSearch: EditText? = null
    private var mHamburger: DrawerButton? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        mHamburger = findViewById(R.id.hamburger)
        mSearch = findViewById(R.id.search)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        swapFragment(R.id.container,mMapFragment)

        mNavigationView.apply {
            this!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.map -> swapFragment(R.id.container, mMapFragment)
                        R.id.charts -> swapFragment(R.id.container, mOverallFragment)
                        R.id.settings -> Toast.makeText(this@MainActivity ," settings" , Toast.LENGTH_SHORT).show()
                        R.id.settings1 -> Toast.makeText(this@MainActivity  ," settings1" , Toast.LENGTH_SHORT).show()
                        R.id.map1 -> Toast.makeText(this@MainActivity  ," map1" , Toast.LENGTH_SHORT).show()
                    }

                    mHamburger?.changeState()
                    return true
                }
            })
            this.menu.getItem(0).isChecked = true
        }

        mSearch?.apply {
            this.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    swapFragment(R.id.container, SearchFragment())
                }
                true
            }
        }
        mHamburger?.apply {
            this.setDrawerLayout(mDrawerLayout!!)
            this.getDrawerLayout()?.addDrawerListener(mHamburger!!)
            this.setDrawerArrowDrawable(DrawerArrowDrawable(this@MainActivity))
            this.setOnClickListener {
                if (!mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    mHamburger?.changeState()
                }
            }
        }

    }

    override fun onBackPressed() {
        if (mDrawerLayout?.isDrawerOpen(Gravity.LEFT)!!) {
            mDrawerLayout?.closeDrawer(Gravity.LEFT)
        }else{
            Log.d("TAG" , "BACK")
            if (supportFragmentManager.backStackEntryCount == 1){
                this.finish()
            }else{
                supportFragmentManager.popBackStack()
            }
        }
    }

    fun swapFragment(res: Int, fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(res,fragment)
        transaction.commit()
    }


}
