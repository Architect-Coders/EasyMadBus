package com.developer.ivan.easymadbus.framework

import android.graphics.Point
import android.location.Location
import android.os.Bundle
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapManager(private val mapView: MapView?) : OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var mapPoints: List<BusStop> = listOf()

    companion object {
        const val DEFAULT_ZOOM = 16f
    }

    fun onCreate(savedInstanceState: Bundle?) {
        mapView?.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@MapManager)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0
        configureMap()

    }

    private fun configureMap() {
        mMap?.uiSettings?.isZoomControlsEnabled = false

        mapPoints.forEach {busStop->
            mMap?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        busStop.geometry.coordinates[1],
                        busStop.geometry.coordinates[0]
                    )
                ).title(buildString {
                    append(busStop.name)
                })
            )
        }

    }

    fun setPoints(busStops: List<BusStop>) {
        mapPoints = busStops.map { it.copy() }
        configureMap()
        moveToLocation(Constants.EMTApi.MADRID_LOC)
    }

    fun moveToLocation(location: LatLng) {
        mMap?.moveCamera(
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
        mMap = null
    }

    fun onLowMemory() {
        mapView?.onLowMemory()
    }
}