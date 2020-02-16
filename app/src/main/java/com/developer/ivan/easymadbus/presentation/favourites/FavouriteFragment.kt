package com.developer.ivan.easymadbus.presentation.favourites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.App

import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.*
import com.developer.ivan.easymadbus.presentation.adapters.FavouritesAdapter
import com.developer.ivan.usecases.*
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    private val mViewModel: FavouriteViewModel by lazy {
        getViewModel { ((requireActivity().application) as App).component.favouriteViewModel }
    }
    private lateinit var mAdapter: FavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_favourite)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStates()
        initUI()
        initListeners()

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

    private fun initListeners()
    {
        swipeRefresh.setOnRefreshListener {
            mViewModel.obtainInfo()
        }
    }

    private fun handleFailure(failure: Failure?) {
        swipeRefresh.isRefreshing = false
    }

    private fun renderFavouriteState(state: FavouriteViewModel.FavouriteScreenState?) {
        swipeRefresh.isRefreshing=false
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

}
