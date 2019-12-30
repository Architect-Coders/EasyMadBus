package com.developer.ivan.easymadbus.framework

import android.os.Bundle
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapManager(private val mapView: MapView?,
                 private val onMapReadyCallback: (GoogleMap?)->Unit) : OnMapReadyCallback {
    private var _mMap: GoogleMap? = null

    private var mapPoints: List<BusStop> = listOf()


    companion object {
        const val DEFAULT_ZOOM = 15f
    }

    fun onCreate(savedInstanceState: Bundle?) {
        mapView?.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@MapManager)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

        onMapReadyCallback.invoke(p0)
        _mMap = p0
        configureMap()

    }

    private fun configureMap() {
        _mMap?.uiSettings?.isZoomControlsEnabled = false

    }
    fun moveToDefaultLocation(){
        moveToLocation(Constants.EMTApi.MADRID_LOC)
    }

    fun moveToLocation(location: LatLng) {
        _mMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location, DEFAULT_ZOOM
            )
        )

    }

    fun onResume() {
        mapView?.onResume()
    }

    fun onPause() {
        mapView?.onPause()
    }

    fun onStart() {
        mapView?.onStart()
    }

    fun onStop() {
        mapView?.onStop()
    }


    fun onDestroy() {
        mapView?.onDestroy()
        _mMap = null
    }

    fun onLowMemory() {
        mapView?.onLowMemory()
    }
}