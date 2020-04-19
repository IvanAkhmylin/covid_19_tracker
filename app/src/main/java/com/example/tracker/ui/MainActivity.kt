package com.example.tracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.tracker.App.Companion.hideKeyboard
import com.example.tracker.App.Companion.showKeyboard
import com.example.tracker.Constants
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
    private var mToolLayout: LinearLayout? = null
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
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mToolLayout = findViewById(R.id.tool_layout)
        mHamburger?.apply {
            this.setDrawerLayout(mDrawerLayout!!)
            this.getDrawerLayout()?.addDrawerListener(mHamburger!!)
            this.setDrawerArrowDrawable(DrawerArrowDrawable(this@MainActivity))
        }

        mSearch = findViewById(R.id.search)
        mNavigationView.apply {
            this!!.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.charts ->
                        swapFragment(R.id.container, mOverallFragment, "OverallFragment" , Constants.ANIM_UP_DOWN)
                    R.id.settings -> Toast.makeText(
                        this@MainActivity,
                        " settings",
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.settings1 -> Toast.makeText(
                        this@MainActivity,
                        " settings1",
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.map1 -> Toast.makeText(this@MainActivity, " map1", Toast.LENGTH_SHORT)
                        .show()
                }
                mHamburger?.changeDrawerState()
                false
            }

        }

        mSearch?.apply {
            this.setOnTouchListener { v, event ->
                if (!mSearch?.isFocused!!) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        swapFragment(R.id.container, SearchFragment(), "SearchFragment" , Constants.ANIM_UP_DOWN)
                    }
                }
                true
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            mHamburger?.changeSearchState(supportFragmentManager.backStackEntryCount)

            if (supportFragmentManager.backStackEntryCount == 1) {
                resetState()
            } else {
                fragmentChanged()
            }
        }
        swapFragment(R.id.container, mMapFragment, "MapFragment" , Constants.ANIM_UP_DOWN)
    }

    @SuppressLint("ResourceAsColor")
    private fun fragmentChanged() {
        val currentFragment = supportFragmentManager
            .getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        mHamburger?.setOnClickListener {
            onBackPressed()
        }
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        if (currentFragment == "SearchFragment") {
            mSearch?.apply {
                this.requestFocus()
                mSearch?.showKeyboard()
                mToolLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.green
                    )
                )
            }
        } else {
            mSearch?.apply {
                this.animate()
                    .translationX(1000F)
                    .alpha(1f)
                    .start()
            }
        }
    }

    private fun resetState() {
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        mHamburger?.apply {
            this.setOnClickListener {
                this.changeDrawerState()
            }
        }

        mToolLayout?.setBackgroundColor(0)
        mSearch?.apply {
            this.text.clear()
            this.animate()
                .translationX(0f)
                .alpha(1f)
                .start()

        }

        mSearch?.apply {
            this.clearFocus()
            this.hideKeyboard()
        }
    }


    override fun onBackPressed() {
        if (mDrawerLayout?.isDrawerOpen(Gravity.LEFT)!!) {
            mDrawerLayout?.closeDrawer(Gravity.LEFT)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1) {
                this.finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    fun swapFragment(res: Int, fragment: Fragment, tag: String, animationMode: String) {
        val transaction = supportFragmentManager.beginTransaction()
        if (Constants.ANIM_UP_DOWN == animationMode) {
            transaction.setCustomAnimations(
                R.anim.enter_up,
                R.anim.exit_down,
                R.anim.pop_enter_up,
                R.anim.pop_exit_down
            )
        }else if (Constants.ANIM_SLIDE_LEFT == animationMode){
            transaction.setCustomAnimations(
                R.anim.enter_left,
                R.anim.exit_right,
                R.anim.pop_enter_left,
                R.anim.pop_exit_right
            )
        }
        transaction.addToBackStack(tag)
        transaction.replace(res, fragment)
        transaction.commit()
    }


}
