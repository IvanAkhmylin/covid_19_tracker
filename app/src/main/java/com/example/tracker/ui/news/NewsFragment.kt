package com.example.tracker.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tracker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NewsFragment : Fragment() {
    private var mViewPager: ViewPager2? = null
    private var mTabLayout: TabLayout? = null
    private var fragmentList: ArrayList<Fragment>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_news, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        initFragmentList()
        mViewPager = v.findViewById(R.id.news_viewpager)
        mTabLayout = v.findViewById(R.id.news_tab)
        mViewPager?.adapter = (NewsViewPagerAdapter(fragmentList!! ,childFragmentManager , lifecycle))
        TabLayoutMediator(mTabLayout!!, mViewPager!!){ tab , pos ->
                tab.text = fragmentList?.get(pos)?.arguments?.getString(NewsListFragment.QUERY, "Default")
        }.attach()

    }

    private fun initFragmentList() {
        fragmentList = ArrayList()
        fragmentList?.add(NewsListFragment.newInstance("World"))
        fragmentList?.add(NewsListFragment.newInstance("Europe"))
        fragmentList?.add(NewsListFragment.newInstance("Asia"))
        fragmentList?.add(NewsListFragment.newInstance("Africa"))
        fragmentList?.add(NewsListFragment.newInstance("North America"))
        fragmentList?.add(NewsListFragment.newInstance("South America"))
        fragmentList?.add(NewsListFragment.newInstance("Australia"))
        fragmentList?.add(NewsListFragment.newInstance("Antarctica"))
    }


}
