package com.developer.ivan.easymadbus.presentation.map


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.RequestManager
import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.*
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.repository.IBusRepository
import com.developer.ivan.easymadbus.domain.uc.GetBusStops
import com.developer.ivan.easymadbus.domain.uc.GetCoarseLocation
import com.developer.ivan.easymadbus.domain.uc.GetFineLocation
import com.developer.ivan.easymadbus.domain.uc.GetToken
import com.developer.ivan.easymadbus.framework.MapManager
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.easymadbus.framework.PermissionRequester
import com.developer.ivan.easymadbus.framework.PlayServicesLocationDataSource
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_map.*


class BusMapFragment : Fragment() {

    private var mapManager: MapManager? = null
    private lateinit var mViewModel: BusMapViewModel
    private var mClusterManager: ClusterManager<BusStop>? = null
    private lateinit var requestManager: PermissionRequester
    private var googleMap: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {

            findViewById<MapView>(R.id.map)?.let { mapView ->
                mapManager =
                    MapManager(mapView, ::handleMapReady).apply { onCreate(savedInstanceState) }

            }
        }
    }


    private fun handleMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        mClusterManager = ClusterManager(context, this.googleMap)
        mClusterManager!!.setAnimation(true)
        this.googleMap?.setOnCameraIdleListener(mClusterManager)

        mViewModel.busStops()
        mViewModel.fusedLocation()
    }

    private fun setPoints(listPoints: List<BusStop>) {

        listPoints.forEach { mClusterManager?.addItem(it) }
//        mapManager?.moveToDefaultLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getViewModel()

        initListeners()

        requestManager = PermissionRequester(requireActivity())


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
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun renderBusState(busStopScreenState: BusMapViewModel.BusStopScreenState) {


        when (busStopScreenState) {
            BusMapViewModel.BusStopScreenState.Loading -> progressBar.show()
            BusMapViewModel.BusStopScreenState.Failure -> {
                progressBar.hide()
            }
            is BusMapViewModel.BusStopScreenState.ShowBusStops -> {
                progressBar.hide()

                setPoints(busStopScreenState.busStops)
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
                LatLng(busStopScreenState.location.latitude, busStopScreenState.location.longitude)
            )
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

    private fun getViewModel() {
        val repository = IBusRepository.BusRepository(
            retrofit,
            ServerMapper,
            application = requireActivity().application as App
        )

        mViewModel = ViewModelProvider(
            this,
            BusMapViewModel.BusMapViewModelFactory(
                GetBusStops(
                    repository
                ),
                GetToken(
                    repository
                ),
                GetCoarseLocation(PlayServicesLocationDataSource(application = requireActivity().application)),
                PlayServicesLocationDataSource(application = requireActivity().application),
                PermissionChecker(application = requireActivity().application)
            )
        )[BusMapViewModel::class.java]
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
