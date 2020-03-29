package com.example.tracker.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.tracker.R
import com.example.tracker.model.CountriesStatisticModel
import com.example.tracker.ui.MainActivity
import com.example.tracker.viewmodel.StatisticViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider


class MapStatisticFragment : Fragment() {
    private var mMapView: MapView? = null
    private val mViewModel: StatisticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(getString(R.string.yandexApiKey))
        MapKitFactory.initialize(activity!!)
    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
        MapKitFactory.getInstance().onStart()
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
        mViewModel.mCountriesStatistic.observe(viewLifecycleOwner,  Observer {
            addMarkersToMap(it)
        })
    }

    private fun addMarkersToMap(it: List<CountriesStatisticModel>) {
        it.forEach {
            val v = layoutInflater.inflate(R.layout.item_layout, null)
            mMapView?.map?.mapObjects?.addPlacemark(Point(it.countryInfo.lat!!,  it.countryInfo.long!!),
                ViewProvider(v, false))
        }
    }

    private fun init(v: View?) {
        mMapView = v?.findViewById(R.id.map_view)

    }


}
