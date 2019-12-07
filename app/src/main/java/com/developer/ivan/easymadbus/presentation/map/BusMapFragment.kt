package com.developer.ivan.easymadbus.presentation.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.inflateFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class BusMapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap?=null
    private var mMapView: MapView?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {
            mMapView = findViewById<MapView>(R.id.map).apply {
                onCreate(savedInstanceState)
                getMapAsync(this@BusMapFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }


    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0


        mMap?.uiSettings?.isZoomControlsEnabled = false
        mMap?.addMarker(MarkerOptions().position(LatLng(40.4165000,-3.7025600)))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.4165000,-3.7025600), 18f))

    }

    private fun configureMap()
    {
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }


}
