package com.developer.ivan.easymadbus.framework

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class MapManager(private val mapView: MapView?) : OnMapReadyCallback
{
    private var mMap: GoogleMap?=null

    companion object
    {
        const val DEFAULT_ZOOM = 18f
    }

    fun onCreate(savedInstanceState: Bundle?)
    {
        mapView?.apply {
                onCreate(savedInstanceState)
                getMapAsync(this@MapManager)
            }
    }

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0
        configureMap()

    }

    private fun configureMap()
    {
        mMap?.uiSettings?.isZoomControlsEnabled = false
    }

    fun moveToLocation(location: Location)
    {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude), DEFAULT_ZOOM))

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
        mMap=null
    }

    fun onLowMemory() {
        mapView?.onLowMemory()
    }
}