package com.developer.ivan.easymadbus.presentation.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.inflateFragment
import com.developer.ivan.easymadbus.framework.MapManager
import com.google.android.gms.maps.*


class BusMapFragment : Fragment() {

    private var mMap: GoogleMap?=null
    private var mapManager: MapManager?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {
            findViewById<MapView>(R.id.map).also {

                mapManager = MapManager(it).also {mapManager->
                    mapManager.onCreate(savedInstanceState)
                }
            }
        }
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


    override fun onDestroy() {
        super.onDestroy()
        mapManager?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapManager?.onLowMemory()
    }



}
