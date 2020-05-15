package com.example.tracker.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tracker.R
import com.example.tracker.ui.MainActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_map_box.*


class MapBoxFragment : Fragment() {
    private var mMapBox : com.mapbox.mapboxsdk.maps.MapView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance((activity as MainActivity), getString(R.string.map_box_api_key))
        val v = inflater.inflate(R.layout.fragment_map_box, container, false)
        init(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun init(v: View) {
        mMapBox = v.findViewById(R.id.mapView)
        mMapBox?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT, object : Style.OnStyleLoaded{
                override fun onStyleLoaded(style: Style) {


                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mMapBox?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapBox?.onResume()
    }

     override fun onPause() {
        super.onPause()
        mMapBox?.onPause()
    }

     override fun onStop() {
        super.onStop()
        mMapBox?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapBox?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMapBox?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapBox?.onSaveInstanceState(outState)

    }
}
