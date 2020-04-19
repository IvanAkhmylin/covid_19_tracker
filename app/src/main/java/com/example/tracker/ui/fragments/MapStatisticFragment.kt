package com.example.tracker.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.App
import com.example.tracker.Constants
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.ui.MainActivity
import com.example.tracker.viewmodel.StatisticViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapStatisticFragment : Fragment() {
    private var mMapView: MapView? = null
    private val mViewModel: StatisticViewModel by viewModels()
    private var data: ArrayList<CountriesStatisticModel>? = null

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
            val dataModel = it?.firstOrNull {
                it.countryInfo.lat == (p0 as PlacemarkMapObject).geometry.latitude &&
                        it.countryInfo.long == p0.geometry.longitude
            }
            (activity!! as MainActivity).swapFragment(R.id.container,
                CountriesDetailFragment(dataModel), "DetailFragment" , Constants.ANIM_SLIDE_LEFT)
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
        mMapView?.map!!.apply {
            this.isNightModeEnabled = true
            this.isRotateGesturesEnabled = false
        }
        MapKitFactory.getInstance().onStart()
    }
    @SuppressLint("CommitPrefEdits")
    override fun onPause() {
        super.onPause()
        val  mPref = (activity!! as MainActivity).getSharedPreferences("MAP",Context.MODE_PRIVATE)
        mPref?.edit().apply{
            this?.putFloat(Constants.MAP_ZOOM_KEY, mMapView?.map!!.cameraPosition.zoom)
            this?.putFloat(Constants.MAP_TARGET_LAT, mMapView?.map!!.cameraPosition.target.latitude.toFloat())
            this?.putFloat(Constants.MAP_TARGET_LON, mMapView?.map!!.cameraPosition.target.longitude.toFloat())
            this?.putFloat(Constants.MAP_ZOOM_AZIMUTH, mMapView?.map!!.cameraPosition.azimuth)
            this?.putFloat(Constants.MAP_ZOOM_TILT, mMapView?.map!!.cameraPosition.tilt)
            this?.apply()
        }
        mMapView?.visibility = View.INVISIBLE
    }
    override fun onResume() {
        super.onResume()
        val mPref = (activity!! as MainActivity).getSharedPreferences("MAP", Context.MODE_PRIVATE)
        mMapView?.apply {
            this.visibility = View.VISIBLE
            this.map.move(CameraPosition(Point(
                mPref.getFloat(Constants.MAP_TARGET_LAT, 0f).toDouble(),
                mPref.getFloat(Constants.MAP_TARGET_LON, 0f).toDouble()),
                mPref.getFloat(Constants.MAP_ZOOM_KEY, 0f),
                mPref.getFloat(Constants.MAP_ZOOM_AZIMUTH, 0f),
                mPref.getFloat(Constants.MAP_ZOOM_TILT , 0f))
            )
        }
    }

    override fun onStop() {
        mMapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}
