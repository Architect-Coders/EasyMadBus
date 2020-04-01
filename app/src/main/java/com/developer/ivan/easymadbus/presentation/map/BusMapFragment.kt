package com.developer.ivan.easymadbus.presentation.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.*
import com.developer.ivan.easymadbus.framework.MapManager
import com.developer.ivan.easymadbus.framework.PermissionRequester
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map.*


class BusMapFragment : Fragment() {

    private var mapManager: MapManager? = null
    private lateinit var component: BusMapFragmentComponent

    private val mViewModel: BusMapViewModel by lazy { getViewModel { component.busMapViewmodel } }
    private lateinit var requestManager: PermissionRequester


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = ((requireActivity().application) as App).component.plus(BusMapFragmentModule())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {

            findViewById<MapView>(R.id.map)?.let { mapView ->
                mapManager =
                    MapManager(
                        requireActivity().application,
                        mapView,
                        ::handleMapReady,
                        ::handleMarkClick,
                        ::handleInfoWindowClick
                    ).apply { onCreate(savedInstanceState) }

            }
        }
    }

    private fun handleMarkClick(markId: String, stopId: String) {
        mViewModel.clickInMark(markId, stopId)
    }

    private fun handleInfoWindowClick(markId: String, busData: Pair<UIBusStop, UIStopFavourite?>) {
        mViewModel.clickInInfoWindow(markId, busData)
    }


    private fun handleMapReady() {
        mViewModel.busStops()
        mViewModel.fusedLocation()
    }

    private fun setPoints(listPoints: List<UIBusStop>) {

        mapManager?.addPoints(listPoints)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requestManager = PermissionRequester(requireActivity())

        initListeners()
        initStates()

    }

    private fun initStates() {
        mViewModel.busState.observe(viewLifecycleOwner, Observer {
            renderBusState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })
    }

    private fun initListeners() {
        fabLocation.setOnClickListener {

            mViewModel.fineLocation()

        }
    }

    private fun handleFailure(failure: Failure?) {

        when(failure)
        {
            is BusMapViewModel.BusMarkError -> {
               mapManager?.findMarker(failure.markId)?.let {marker->
                    mViewModel.updateMarkerError(marker)
                }
            }
            else-> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

        }
    }

    private fun renderBusState(busStopScreenState: BusMapViewModel.BusStopScreenState) {


        when (busStopScreenState) {
            BusMapViewModel.BusStopScreenState.Loading -> progressBar.show()
            is BusMapViewModel.BusStopScreenState.ShowBusStops -> {
                progressBar.hide()

                setPoints(busStopScreenState.uiBusStop)
            }
            is BusMapViewModel.BusStopScreenState.RequestCoarseLocation -> {
                requestManager.request(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    ::handleCoarsePermission
                )
            }
            is BusMapViewModel.BusStopScreenState.RequestFineLocation -> {
                requestManager.request(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    ::handleFinePermission
                )
            }

            is BusMapViewModel.BusStopScreenState.ShowFusedLocation -> mapManager?.moveToLocation(
                LatLng(busStopScreenState.location.lat, busStopScreenState.location.lng)
            )

            is BusMapViewModel.BusStopScreenState.ShowBusStopInfo -> {

                mapManager?.findMarker(busStopScreenState.markId)?.let {

                    mViewModel.updateMarkerInfo(
                        it,
                        busStopScreenState.busData,
                        busStopScreenState.arrives
                    )

                }
            }

            is BusMapViewModel.BusStopScreenState.UpdateMarkerInfoWindow -> {
                busStopScreenState.marker.showInfoWindow()
            }
        }
    }

    private fun handleCoarsePermission(granted: Boolean) {
        if (granted)
            mViewModel.fusedLocation()
    }

    private fun handleFinePermission(granted: Boolean) {
        if (granted)
            mViewModel.fineLocation()
    }

    override fun onResume() {
        super.onResume()
        mapManager?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapManager?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapManager?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapManager?.onStop()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mapManager?.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapManager?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
