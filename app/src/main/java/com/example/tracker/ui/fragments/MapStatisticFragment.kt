package com.example.tracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.App
import com.example.tracker.view.DrawerButton
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.ui.MainActivity
import com.example.tracker.viewmodel.StatisticViewModel
import com.google.android.material.navigation.NavigationView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapStatisticFragment : Fragment() {

    private var mMapView: MapView? = null
    private val mViewModel: StatisticViewModel by viewModels()
    private var data: ArrayList<CountriesStatisticModel>? = null
    private var mSearch: EditText? = null
    private var mHamburger: DrawerButton? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null

    private lateinit var listener: MapObjectTapListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(getString(R.string.yandexApiKey))
        MapKitFactory.initialize(activity!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.countries_statistic_layout, container, false)
        init(v)
        return v
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "WrongConstant")
    private fun init(v: View?) {
        mMapView = v?.findViewById(R.id.map_view)
        mHamburger = v?.findViewById(R.id.hamburger)
        mSearch = v?.findViewById(R.id.search)
        mDrawerLayout = v?.findViewById(R.id.drawer_layout)
        mNavigationView = v?.findViewById(R.id.nav_view)

        mNavigationView.apply {
            this!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.map -> (activity!! as MainActivity).swapFragment(R.id.container, MapStatisticFragment())
                        R.id.charts -> (activity!! as MainActivity).swapFragment(R.id.container, OverallStatisticFragment())
                        R.id.settings -> Toast.makeText(activity!! ," settings" , Toast.LENGTH_SHORT).show()
                        R.id.settings1 -> Toast.makeText(activity!! ," settings1" , Toast.LENGTH_SHORT).show()
                        R.id.map1 -> Toast.makeText(activity!! ," map1" , Toast.LENGTH_SHORT).show()
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
                    (activity!! as MainActivity).swapFragment(R.id.container, SearchFragment())
                }
                true
            }
        }
        mHamburger?.apply {
            this.setDrawerLayout(mDrawerLayout!!)
            this.getDrawerLayout()?.addDrawerListener(mHamburger!!)
            this.setDrawerArrowDrawable(DrawerArrowDrawable(activity!!))
            this.setOnClickListener {
                if (!mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    mHamburger?.changeState()
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.mCountriesStatistic.observe(viewLifecycleOwner, Observer {
            listener = getTapListener(it)
            addPlaceMarkToMap(it)
        })
    }

    private fun getTapListener(it: List<CountriesStatisticModel>?): MapObjectTapListener {
        return MapObjectTapListener { p0, p1 ->
            val dataModel = it?.filter {
                it.countryInfo.lat == (p0 as PlacemarkMapObject).geometry.latitude &&
                        it.countryInfo.long == p0.geometry.longitude
            }?.firstOrNull()
            CountriesDetailDialog(dataModel).show(activity!!.supportFragmentManager, "Detail")
            true
        }
    }

    private fun addPlaceMarkToMap(it: List<CountriesStatisticModel>) {
        data?.addAll(it)
        it.forEach {
            val a = mMapView?.map?.mapObjects?.apply {
                this.addPlacemark(
                    Point(it.countryInfo.lat!!, it.countryInfo.long!!),
                    ImageProvider.fromBitmap(
                        App.getBitmapFromVectorDrawable(
                            activity!!,
                            R.drawable.ic_place
                        ), false, ""
                    )
                ).addTapListener(listener)
            }
        }
    }



    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mMapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}
