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
import com.developer.ivan.easymadbus.core.getViewModel
import com.developer.ivan.easymadbus.core.hide
import com.developer.ivan.easymadbus.core.inflateFragment
import com.developer.ivan.easymadbus.core.show
import com.developer.ivan.easymadbus.framework.IMapManager
import com.developer.ivan.easymadbus.framework.OnMapEvent
import com.developer.ivan.easymadbus.framework.OnMapReady
import com.developer.ivan.easymadbus.framework.PermissionRequester
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.developer.ivan.usecases.GetCoarseLocation
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map.*


class BusMapFragment : Fragment(), OnMapReady, OnMapEvent {

    private var mapManager: IMapManager? = null
    private var mapView: MapView? = null
    private lateinit var component: BusMapFragmentComponent

    private val mViewModel: BusMapViewModel by lazy { getViewModel { component.busMapViewmodel } }
    private lateinit var requestManager: PermissionRequester


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {

            mapView = findViewById(R.id.map)
            mapView?.let { mapView ->


                component =
                    ((requireActivity().application) as App).component.plus(BusMapFragmentModule())

                mapManager = component.mapManager.apply { onCreate(savedInstanceState) }

                mapView.apply {
                    onCreate(savedInstanceState)
                    getMapAsync(mapManager)
                }


            }
        }
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

        mapManager?.setMapEventsListener(this)
        mapManager?.setMapReadyListener(this)

    }

    private fun handleFailure(failure: Failure?) {

        when (failure) {
            is BusMapViewModel.BusMarkError -> {
                mapManager?.findMarker(failure.markId)?.let { marker ->
                    mViewModel.updateMarkerError(marker)
                }
            }
            is GetCoarseLocation.NoLocation -> {
                mapManager?.moveToDefaultLocation()
            }
            else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

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
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun setPoints(listPoints: List<UIBusStop>) {

        mapManager?.addPoints(listPoints)
    }

    override fun onMapReadyCallback() {
        mViewModel.busStops()
        mViewModel.fusedLocation()
    }

    override fun onMarkerClick(marker: String, snippet: String) {
        mViewModel.clickInMark(marker, snippet)
    }

    override fun onInfoWindowClick(markerId: String, data: Pair<UIBusStop, UIStopFavourite?>) {
        mViewModel.clickInInfoWindow(markerId, data)
    }
}
