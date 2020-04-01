package com.example.tracker.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tracker.App
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.viewmodel.StatisticViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapStatisticFragment : Fragment() {
    private var mMapView: MapView? = null
    private val mViewModel: StatisticViewModel by viewModels()
    private var data: ArrayList<CountriesStatisticModel>? = null
    private val listener = (MapObjectTapListener { p0, p1 ->
        val a = data?.filter {
            it.countryInfo.lat == p1.latitude
        }
        Log.d("TAG" , a?.size.toString())
        CountriesDetailDialog().show(activity!!.supportFragmentManager, "Detail")
        true
    })

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.mCountriesStatistic.observe(viewLifecycleOwner, Observer {
            data?.addAll(it)
            addPlaceMarkToMap(it)
        })
    }

    private fun addPlaceMarkToMap(it: List<CountriesStatisticModel>) {
        it.forEach {
            mMapView?.map?.mapObjects?.apply {
                this.addPlacemark(
                    Point(it.countryInfo.lat!!, it.countryInfo.long!!),
                    ImageProvider.fromBitmap(
                        App.getBitmapFromVectorDrawable(
                            activity!!,
                            R.drawable.ic_place
                        ), false, ""
                    )
                ).addTapListener(listener)
                this.userData = it
            }
        }
    }

    private fun init(v: View?) {
        mMapView = v?.findViewById(R.id.map_view)

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
