package com.developer.ivan.easymadbus.presentation.favourites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.App

import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.hide
import com.developer.ivan.easymadbus.core.inflateFragment
import com.developer.ivan.easymadbus.core.retrofit
import com.developer.ivan.easymadbus.core.show
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.easymadbus.framework.datasource.PlayServicesLocationDataSource
import com.developer.ivan.easymadbus.framework.datasource.RetrofitDataSource
import com.developer.ivan.easymadbus.framework.datasource.RoomDataSource
import com.developer.ivan.easymadbus.presentation.adapters.FavouritesAdapter
import com.developer.ivan.easymadbus.presentation.map.BusMapViewModel
import com.developer.ivan.usecases.*
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    private lateinit var mViewModel: FavouriteViewModel
    private lateinit var mAdapter: FavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_favourite)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel()
        initStates()
        initUI()

        mViewModel.obtainInfo()
    }

    private fun initStates()
    {
        mViewModel.favouriteState.observe(viewLifecycleOwner, Observer {
            renderFavouriteState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })
    }

    private fun initUI()
    {
        mAdapter = FavouritesAdapter()
        rcvFavourites.adapter = mAdapter
    }

    private fun handleFailure(failure: Failure?) {

    }

    private fun renderFavouriteState(state: FavouriteViewModel.FavouriteScreenState?) {
        when(state)
        {
            is FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteInfo -> {
                progressBar.hide()
                mAdapter.updateItems(state.busData)
            }
            is FavouriteViewModel.FavouriteScreenState.ShowBusStopFavouriteLine -> {
                progressBar.hide()

                mAdapter.updateItem(state.busData)
            }
            is FavouriteViewModel.FavouriteScreenState.Loading -> {
                progressBar.show()
            }
        }
    }

    private fun getViewModel() {
        val repository = BusRepository(
            RetrofitDataSource(retrofit, ServerMapper),
            RoomDataSource((requireActivity().application as App).database)
        )

        mViewModel = ViewModelProvider(
            this,
            FavouriteViewModel.FavouriteViewModelFactory(
                GetToken(
                    repository
                ),
                GetBusStopTime(
                    repository
                ),
                GetBusAndStopsFavourites(
                    repository
                )
            )
        )[FavouriteViewModel::class.java]
    }

}
