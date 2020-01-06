package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.os.Bundle
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.easymadbus.domain.models.Arrive
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.presentation.map.BusInfoWindow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapManager(private val application: Application,
                 private val mapView: MapView?,
                 private val onMapReadyCallback: (GoogleMap?)->Unit,
                 private val onMarkerClick: (String,String)->Unit) : OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private var _mMap: GoogleMap? = null

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
        _mMap?.setInfoWindowAdapter(BusInfoWindow(application))
        _mMap?.setOnMarkerClickListener(this)

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

    override fun onMarkerClick(p0: Marker?): Boolean{

        return if(p0!=null){
            val gsonObject = Gson().fromJson<BusStop>(p0.snippet, object: TypeToken<BusStop>(){}.type)
            gsonObject?.node?.let { onMarkerClick.invoke(p0.id, it) }
            false
        }else
            true
    }


}