package com.example.tracker.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.decimalFormatter
import com.example.tracker.Utils.ExpansionUtils.isDarkThemeOn
import com.example.tracker.model.Country
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.countries.CountriesViewModel
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.TriangleEdgeTreatment
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField
import kotlinx.android.synthetic.main.loading_and_error_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapFragment : Fragment() {
    private var mMapBox: com.mapbox.mapboxsdk.maps.MapView? = null
    private lateinit var mViewModel: CountriesViewModel
    private var countriesList = ArrayList<Country>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance((activity as MainActivity), getString(R.string.map_box_api_key))
        val v = inflater.inflate(R.layout.fragment_map_box, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        mViewModel =
            ViewModelProvider(requireActivity() as MainActivity).get(CountriesViewModel::class.java)

        mMapBox = v.findViewById(R.id.mapView)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getCountriesStatistic()
        }

        mViewModel.mCountriesStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    mMapBox?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    initMap()
                    not_find_data.visibility = View.GONE
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    mMapBox?.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    mMapBox?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                }

            }
        })
    }


    private fun initMap() {
        mMapBox?.getMapAsync { map ->
            val style = if (requireContext().isDarkThemeOn()) {
                Style.DARK
            } else {
                Style.DARK
            }

            map.setStyle(style)

            map.getStyle {
                val mapText: Layer = it.getLayer("country-label")!!
                mapText.setProperties(textField("{name_${requireContext().getString(R.string.app_locale)}}"));
            }

            mViewModel.mFilteredData.observe(viewLifecycleOwner, Observer { list ->
                countriesList.clear()
                countriesList.addAll(list)
                list.forEach { country ->
                    map.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    country.countryInfo.lat!!.toDouble(),
                                    country.countryInfo.long!!.toDouble()
                                )
                            )
                            .title(country.country)
                    )

                }
            })

            map.setOnInfoWindowClickListener { marker ->
                val countyHistoric = mViewModel.getHistoric(marker.title)
                if (countyHistoric != null) {
                    Navigation.findNavController(requireActivity(), R.id.nav_host)
                        .navigate(
                            R.id.action_countries_to_detailCountryFragment,
                            bundleOf(
                                Constants.COUNTRIES_NAME to marker.title,
                                Constants.HISTORIC_OF_COUNTRIES to countyHistoric
                            )
                        )
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.error_getting_historic_data),
                        Toast.LENGTH_LONG
                    ).show()
                }
                true
            }

            map.infoWindowAdapter = MapboxMap.InfoWindowAdapter { marker ->
                val data = countriesList.filter { data ->
                    data.country == marker.title
                }.firstOrNull()

                val view = layoutInflater.inflate(R.layout.info_window_layout, null)
                val size = resources.getDimension(R.dimen.tooltip_size)
                val triangleEdgeTreatment =
                    TriangleEdgeTreatment(size, false)

                val cardView: MaterialCardView = view.findViewById(R.id.map_info_card)
                cardView.setShapeAppearanceModel(
                    cardView.getShapeAppearanceModel()
                        .toBuilder()
                        .setBottomEdge(triangleEdgeTreatment)
                        .build()
                )

                view.findViewById<SimpleDraweeView>(R.id.flag).setImageURI(data?.countryInfo?.flag)
                view.findViewById<TextView>(R.id.country).text = data?.country
                view.findViewById<TextView>(R.id.cases).text = data?.cases?.decimalFormatter()
                view.findViewById<TextView>(R.id.deaths).text = data?.deaths?.decimalFormatter()
                view.findViewById<TextView>(R.id.recovered).text =
                    data?.recovered?.decimalFormatter()


                val moveCameraTo = CameraPosition.Builder()
                    .target(LatLng(marker.position.latitude, marker.position.longitude))
                    .build()
                map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCameraTo), 200)

                view
            }
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
