package com.developer.ivan.easymadbus.presentation.notifications


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.getViewModel
import com.developer.ivan.easymadbus.core.hide
import com.developer.ivan.easymadbus.core.inflateFragment
import com.developer.ivan.easymadbus.core.show
import com.developer.ivan.easymadbus.presentation.adapters.IncidentsAdapter
import kotlinx.android.synthetic.main.fragment_incident.*


class NotificationFragment : Fragment() {

    private val mViewModel: NotificationsViewModel by lazy {
        getViewModel { component.notificationsViewModel }
    }

    private lateinit var mAdapter: IncidentsAdapter
    private lateinit var component: NotificationsFragmentComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component =
            ((requireActivity().application) as App).component.plus(NotificationsFragmentModule())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_incident)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStates()
        initUI()

        mViewModel.obtainInfo()
    }

    private fun initStates() {
        mViewModel.incidentsState.observe(viewLifecycleOwner, Observer {
            renderFavouriteState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })
    }

    private fun initUI() {
        mAdapter = IncidentsAdapter(emptyList())
        rcvIncidents.adapter = mAdapter
    }

    private fun handleFailure(failure: Failure?) {
        Log.e("Failure", failure.toString())
    }

    private fun renderFavouriteState(state: NotificationsViewModel.IncidentsScreenState?) {
        when (state) {
            is NotificationsViewModel.IncidentsScreenState.Loading -> progressBar.show()
            is NotificationsViewModel.IncidentsScreenState.ShowIncidents -> {
                progressBar.hide()
                mAdapter.items = state.incidents
            }
        }
    }


}
