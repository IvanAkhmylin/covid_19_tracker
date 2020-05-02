package com.example.tracker.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tracker.App
import com.example.tracker.Constants
import com.example.tracker.R
import com.example.tracker.model.Country
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.details.CountriesDetailFragment
import com.example.tracker.ui.search.SearchViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment() {
    private var mMapView: MapView? = null
    private lateinit var mMapViewModel: MapViewModel
    private lateinit var mSearchViewModel: SearchViewModel
    private var data: ArrayList<Country>? = null

    private lateinit var listener: MapObjectTapListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(getString(R.string.yandexApiKey))
        MapKitFactory.initialize(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.map_layout, container, false)
        init(v)
        return v
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "WrongConstant")
    private fun init(v: View?) {
        mMapView = v?.findViewById(R.id.map_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            mMapViewModel = ViewModelProvider((requireActivity() as MainActivity)).get(MapViewModel::class.java)
            mSearchViewModel = ViewModelProvider((requireActivity() as MainActivity)).get(SearchViewModel::class.java)

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(R.layout.waiting_dialog)
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    //        mMapViewModel.mSearchCountry.observe(viewLifecycleOwner, Observer {
    //            it?.let {
    //                Handler().postDelayed({
    //                    Log.d("TAG", "${it.countryInfo.lat!!} , ${it.countryInfo.long!!}")
    //                    mMapView?.map!!.move(
    //                        CameraPosition(
    //                            Point(it.countryInfo.lat, it.countryInfo.long),
    //                            5f,
    //                            0f,
    //                            0f
    //                        ), Animation(Animation.Type.SMOOTH, 1f), null
    //                    )
    //                }, 300)
    //            }
    //        })

            mMapViewModel.mCountriesStatistic.observe(viewLifecycleOwner, Observer {
                mSearchViewModel.setCountryList(it as ArrayList<Country>)
                listener = getTapListener(it)
                addPlaceMarkToMap(it)
            })

            mMapViewModel.mFailureMessage.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            })

            mMapViewModel.mShowProgress.observe(viewLifecycleOwner, Observer {
                if (it) {
                    mMapView?.visibility = View.GONE
                    dialog.show()
                } else {
                    mMapView?.visibility = View.VISIBLE
                    dialog.dismiss()
                }
            })
    }

    private fun getTapListener(it: List<Country>?): MapObjectTapListener {
        return MapObjectTapListener { p0, p1 ->
            val dataModel = it?.firstOrNull {
                it.countryInfo.lat == (p0 as PlacemarkMapObject).geometry.latitude &&
                        it.countryInfo.long == p0.geometry.longitude
            }
            (requireActivity() as MainActivity).swapFragment(
                R.id.container,
                CountriesDetailFragment(
                    dataModel
                ), Constants.fragmentDetailMap, Constants.ANIM_SLIDE_LEFT
            )

            true
        }
    }

    private fun addPlaceMarkToMap(it: List<Country>) {
        data?.addAll(it)
        it.forEach {
            mMapView?.map?.mapObjects?.apply {
                this.addPlacemark(
                    Point(it.countryInfo.lat!!, it.countryInfo.long!!),
                    ImageProvider.fromBitmap(
                        App.getBitmapFromVectorDrawable(
                            requireActivity(),
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
        mMapViewModel.mSearchCountry.postValue(null)
        val mPref =
            (requireActivity() as MainActivity).getSharedPreferences("MAP", Context.MODE_PRIVATE)
        mPref?.edit().apply {
            this?.putFloat(Constants.MAP_ZOOM_KEY, mMapView?.map!!.cameraPosition.zoom)
            this?.putFloat(
                Constants.MAP_TARGET_LAT,
                mMapView?.map!!.cameraPosition.target.latitude.toFloat()
            )
            this?.putFloat(
                Constants.MAP_TARGET_LON,
                mMapView?.map!!.cameraPosition.target.longitude.toFloat()
            )
            this?.putFloat(Constants.MAP_ZOOM_AZIMUTH, mMapView?.map!!.cameraPosition.azimuth)
            this?.putFloat(Constants.MAP_ZOOM_TILT, mMapView?.map!!.cameraPosition.tilt)
            this?.apply()
        }
        mMapView?.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        val mPref =
            (requireActivity() as MainActivity).getSharedPreferences("MAP", Context.MODE_PRIVATE)

        mMapView?.apply {
            this.visibility = View.VISIBLE
            this.map.move(
                CameraPosition(
                    Point(
                        mPref.getFloat(Constants.MAP_TARGET_LAT, 0f).toDouble(),
                        mPref.getFloat(Constants.MAP_TARGET_LON, 0f).toDouble()
                    ),
                    mPref.getFloat(Constants.MAP_ZOOM_KEY, 0f),
                    mPref.getFloat(Constants.MAP_ZOOM_AZIMUTH, 0f),
                    mPref.getFloat(Constants.MAP_ZOOM_TILT, 0f)
                )
            )
        }


    }


    override fun onStop() {
        mMapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}
