package com.example.tracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.example.tracker.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mNavController by lazy {
        Navigation.findNavController(this , R.id.nav_host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setSupportActionBar(findViewById(R.id.toolbar))
        setupDrawerLayout()
    }


    private fun setupDrawerLayout() {
        NavigationUI.setupWithNavController(nav_view, mNavController)
        NavigationUI.setupActionBarWithNavController(this, mNavController, drawer_layout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, drawer_layout)
    }

    override fun onBackPressed() {
        if (drawer_layout?.isDrawerOpen(GravityCompat.START)!!){
            drawer_layout?.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

}
