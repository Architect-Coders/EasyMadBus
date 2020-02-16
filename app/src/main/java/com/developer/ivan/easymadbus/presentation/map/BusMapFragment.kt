package com.developer.ivan.easymadbus.presentation.map


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
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.*
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.domain.repository.IBusRepository
import com.developer.ivan.easymadbus.domain.uc.GetBusStops
import com.developer.ivan.easymadbus.domain.uc.GetToken
import com.developer.ivan.easymadbus.framework.MapManager
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.fragment_map.*


class BusMapFragment : Fragment() {

    private var mapManager: MapManager? = null
    private lateinit var mViewModel: BusMapViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflateFragment(R.layout.fragment_map)?.apply {
            findViewById<MapView>(R.id.map).also {

                mapManager = MapManager(it).also { mapManager ->
                    mapManager.onCreate(savedInstanceState)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(savedInstanceState==null)
            getViewModel()


        mViewModel.busState.observe(viewLifecycleOwner, Observer {
            renderBusState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })

    }

    private fun handleFailure(failure: Failure?) {
        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
    }

    private fun renderBusState(busStopScreenState: BusMapViewModel.BusStopScreenState) {


        when(busStopScreenState)
        {
            BusMapViewModel.BusStopScreenState.Loading -> progressBar.show()
            BusMapViewModel.BusStopScreenState.Failure -> {
                progressBar.hide()
            }
            is BusMapViewModel.BusStopScreenState.ShowBusStops -> {
                progressBar.hide()

                mapManager?.setPoints(busStopScreenState.busStops)


            }
        }
    }

    private fun getViewModel() {
        val repository= IBusRepository.BusRepository(retrofit,
            ServerMapper)

        mViewModel = ViewModelProvider(
            this,
            BusMapViewModel.BusMapViewModelFactory(
                GetBusStops(
                    repository
                ),
                GetToken(
                    repository
                )
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


    override fun onDestroy() {
        super.onDestroy()
        mapManager?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapManager?.onLowMemory()
    }


}
