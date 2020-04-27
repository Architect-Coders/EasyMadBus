package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.os.Bundle
import com.developer.ivan.domain.Constants
import com.developer.ivan.easymadbus.presentation.map.customviews.BusInfoWindow
import com.developer.ivan.easymadbus.presentation.map.customviews.ClusterItem
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager

interface IMapManager : OnMapReadyCallback {
    fun setMapReadyListener(listener: OnMapReady)
    fun setMapEventsListener(listener: OnMapEvent)
    fun moveToDefaultLocation()
    fun moveToLocation(location: LatLng)
    fun addPoints(items: List<UIBusStop>)
    fun findMarker(markerId: String): Marker?
    fun onCreate(
        savedInstanceState: Bundle?,
        defaultPoints: List<UIBusStop> = emptyList(),
        mapConfiguration: MapManager.MapConfiguration = MapManager.MapConfiguration()
    )
}

interface OnMapReady {
    fun onMapReadyCallback()

}

interface OnMapEvent {
    fun onMarkerClick(marker: String, snippet: String)
    fun onInfoWindowClick(markerId: String, data: Pair<UIBusStop, UIStopFavourite?>)


}

class MapManager(
    private val application: Application
) :
    GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
    IMapManager {

    class MapConfiguration(
        val isMarkerClickEnable: Boolean = true,
        val isClusterAnimationEnable: Boolean = false
    )


    private var mMapEventListener: OnMapEvent? = null
    private var mMapReadyListener: OnMapReady? = null

    private var _mMap: GoogleMap? = null

    private var mClusterManager: ClusterManager<UIBusStop>? = null

    private var mDefaultPoints = listOf<UIBusStop>()

    private lateinit var mapConfiguration: MapConfiguration


    companion object {
        const val DEFAULT_ZOOM = 15f
    }

    override fun setMapReadyListener(listener: OnMapReady) {
        mMapReadyListener = listener
    }

    override fun setMapEventsListener(listener: OnMapEvent) {
        mMapEventListener = listener
    }

    override fun onCreate(
        savedInstanceState: Bundle?,
        defaultPoints: List<UIBusStop>,
        mapConfiguration: MapConfiguration
    ) {

        this.mDefaultPoints = defaultPoints
        this.mapConfiguration = mapConfiguration
    }

    override fun onMapReady(p0: GoogleMap?) {

        _mMap = p0
        mMapReadyListener?.onMapReadyCallback()
        configureCluster(mapConfiguration)
        configureMap(mapConfiguration)
        addPoints(mDefaultPoints)

    }

    private fun configureCluster(mapConfiguration: MapConfiguration) {
        mClusterManager = ClusterManager(application.applicationContext, _mMap)

        if (mapConfiguration.isClusterAnimationEnable)
            mClusterManager?.setAnimation(true)
        else
            mClusterManager?.setAnimation(false)

        mClusterManager?.renderer =
            ClusterItem(
                application.applicationContext,
                _mMap,
                mClusterManager!!
            )


        _mMap?.setOnCameraIdleListener(mClusterManager)

    }

    private fun configureMap(mapConfiguration: MapConfiguration) {
        _mMap?.uiSettings?.isZoomControlsEnabled = false
        _mMap?.setInfoWindowAdapter(
            BusInfoWindow(
                application
            )
        )

        if (mapConfiguration.isMarkerClickEnable) {
            _mMap?.setOnMarkerClickListener(this)
            _mMap?.setOnInfoWindowClickListener(this)
        } else
            _mMap?.setOnMarkerClickListener { true }


    }

    override fun moveToDefaultLocation() {

        moveToLocation(LatLng(Constants.EMTApi.MADRID_LOC.lat, Constants.EMTApi.MADRID_LOC.lng))
    }

    override fun moveToLocation(location: LatLng) {
        _mMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location, DEFAULT_ZOOM
            )
        )

    }

    override fun addPoints(items: List<UIBusStop>) {
        items.forEach { mClusterManager?.addItem(it) }
        mClusterManager?.cluster()
    }

    override fun findMarker(markerId: String) =
        mClusterManager?.markerCollection?.markers?.find { it.id == markerId }


    override fun onMarkerClick(p0: Marker?): Boolean {

        return if (p0 != null && p0.snippet != null) {
            mMapEventListener?.onMarkerClick(p0.id, p0.snippet)
            false
        } else
            true
    }

    @Suppress("UNCHECKED_CAST")
    override fun onInfoWindowClick(p0: Marker?) {

        p0?.let {
            val tag = p0.tag as? Pair<UIBusStop, UIStopFavourite?>

            tag?.let {
                mMapEventListener?.onInfoWindowClick(p0.id, it)
            }
        }
    }

}
