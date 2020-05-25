package com.example.tracker.ui.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

open class NewsViewPagerAdapter(val fragmentList : ArrayList<Fragment>,fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentList.size - 1
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}