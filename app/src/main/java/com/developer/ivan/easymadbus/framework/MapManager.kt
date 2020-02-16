package com.developer.ivan.easymadbus.framework

import android.graphics.Point
import android.location.Location
import android.os.Bundle
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
        const val DEFAULT_ZOOM = 18f
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

        mapPoints.forEach {
            mMap?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        it.geometry.coordinates[0],
                        it.geometry.coordinates[1]
                    )
                ).title(it.name)
            )
        }

    }

    fun setPoints(busStops: List<BusStop>) {
        mapPoints = busStops.map { it.copy() }
        configureMap()
    }

    fun moveToLocation(location: Location) {
        mMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), DEFAULT_ZOOM
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