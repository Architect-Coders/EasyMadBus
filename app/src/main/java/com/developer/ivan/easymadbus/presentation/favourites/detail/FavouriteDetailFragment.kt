package com.developer.ivan.easymadbus.presentation.favourites.detail


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Constants.Args.ARG_FAVOURITE
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.empty
import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.getViewModel
import com.developer.ivan.easymadbus.core.inflateFragment
import com.developer.ivan.easymadbus.framework.MapManager
import com.developer.ivan.easymadbus.presentation.favourites.detail.customviews.LineDetailCustomView
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.fragment_detail_favourite.*

@Suppress("UNCHECKED_CAST")
class FavouriteDetailFragment : Fragment(), MapManager.OnMapReady {

    private val mViewModel: FavouriteDetailViewModel by lazy {
        getViewModel { component.favouriteDetailViewModel }
    }

    private var mapManager: MapManager? = null


    private lateinit var component: FavouriteDetailComponent

    private lateinit var data: Pair<UIBusStop, UIStopFavourite>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = arguments?.getSerializable(ARG_FAVOURITE) as Pair<UIBusStop, UIStopFavourite>
        component = ((requireActivity().application) as App).component.plus(
            FavouriteDetailFragmentModule(data)
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_detail_favourite)?.apply {
        findViewById<MapView>(R.id.mapDetail)?.let { mapView ->
            mapManager =
                MapManager(
                    requireActivity().application,
                    mapView,
                    MapManager.MapConfiguration(isMarkerClickEnable = false)
                ).apply { onCreate(savedInstanceState,listOf(data.first)) }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStates()
        initListeners()


    }

    private fun initListeners() {
        mapManager?.setMapReadyListener(this)

        swipeRefresh.setOnRefreshListener {
            mViewModel.obtainLineInfo()
        }
    }


    private fun initStates() {
        mViewModel.favouriteDetailState.observe(viewLifecycleOwner, Observer {
            renderFavouriteState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })
    }

    private fun handleFailure(failure: Failure?) {

        swipeRefresh.isRefreshing=false
        Log.e("Failure",failure.toString())
    }


    private fun renderFavouriteState(state: FavouriteDetailViewModel.FavouriteDetailScreenState?) {
        when(state)
        {
            FavouriteDetailViewModel.FavouriteDetailScreenState.Loading -> {}
            is FavouriteDetailViewModel.FavouriteDetailScreenState.ShowBusData -> renderBusData(state.busData)
            is FavouriteDetailViewModel.FavouriteDetailScreenState.ShowBusLine -> renderBusLines(state.busData)
            null -> {}
        }
    }

    private fun renderBusLines(busData: UIBusStop){

        swipeRefresh.isRefreshing=false

        lines.removeAllViews()

        busData.lines.forEach {line->
            lines.addView(
                LineDetailCustomView(
                    requireContext()
                ).apply {
                setLineDetail(
                    line
                )
            })
        }
    }

    private fun renderBusData(busData: Pair<UIBusStop, UIStopFavourite>) {

        swipeRefresh.isRefreshing=false

        busData.first.lines.forEach {line->
            lines.addView(
                LineDetailCustomView(
                    requireContext()
                ).apply {
                setLineDetail(
                    line
                )
            })
        }


        txtTitleDetail.text = busData.first.name
        txtSubtitleDetail.text = if(busData.first.name!=busData.second.name) busData.second.name else String.empty
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


    override fun onMapReadyCallback() {
        mapManager?.moveToLocation(data.first.position)

    }


}
