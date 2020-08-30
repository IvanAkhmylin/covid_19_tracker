package com.example.tracker.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.utils.ExpansionUtils.decimalFormatter
import com.example.tracker.utils.ExpansionUtils.isDarkThemeOn
import com.example.tracker.data.local.entity.Country
import com.example.tracker.databinding.FragmentMapBoxBinding
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
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class MapFragment : BaseFragment() {
    private val mViewModel: CountriesViewModel by injectViewModel()

    private var _binding: FragmentMapBoxBinding? = null
    private val binding get() = _binding

    private var countriesList = ArrayList<Country>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance((activity as MainActivity), getString(R.string.map_box_api_key))

        _binding = FragmentMapBoxBinding.inflate(inflater, container, false)
        val v = binding!!.root
        init(v)
        return v
    }


    private fun init(v: View) {

        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getCountries()
        }

        mViewModel.mStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    binding?.mapView?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    initMap()
                    binding?.mapView?.visibility = View.VISIBLE
                    not_find_data.visibility = View.GONE
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding?.mapView?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                }

            }
        })
    }

    private fun initMap() {
        binding?.mapView?.getMapAsync { map ->
            val style = if (requireContext().isDarkThemeOn()) {
                Style.DARK
            } else {
                Style.LIGHT
            }

            map.setStyle(style)

            map.getStyle {
                val mapText: Layer = it.getLayer("country-label")!!
                mapText.setProperties(textField("{name_${requireContext().getString(R.string.app_locale)}}"));
            }


            mViewModel.mCountries.observe(viewLifecycleOwner, Observer { list ->
                addMarkers(list, map)
            })


            initInfoWindowClickListener(map)
            initInfoWindowCard(map)
        }

    }

    private fun addMarkers(list: List<Country>, map: MapboxMap) {
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
    }

    private fun initInfoWindowClickListener(map: MapboxMap) {
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
    }

    private fun initInfoWindowCard(map: MapboxMap) {
        map.infoWindowAdapter = MapboxMap.InfoWindowAdapter { marker ->
            val data = countriesList.firstOrNull { data ->
                data.country == marker.title
            }

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

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.mapView?.onDestroy()
        _binding = null

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState)
    }
}
