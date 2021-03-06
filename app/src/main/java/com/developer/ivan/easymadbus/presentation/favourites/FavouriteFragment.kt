package com.developer.ivan.easymadbus.presentation.favourites


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.developer.ivan.domain.Constants.Args.ARG_FAVOURITE
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.App
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.*
import com.developer.ivan.easymadbus.presentation.adapters.FavouritesAdapter
import com.developer.ivan.easymadbus.presentation.adapters.SwipeToDeleteCallback
import com.developer.ivan.easymadbus.presentation.dialogs.ConfirmDialog
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.layout_no_favourites.*


class FavouriteFragment : Fragment(), ConfirmDialog.OnActionElementsListener {

    private val mViewModel: FavouriteViewModel by lazy {
        getViewModel { component.favouriteViewModel }
    }

    private lateinit var navController: NavController

    private lateinit var mAdapter: FavouritesAdapter

    private lateinit var component: FavouriteFragmentComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = ((requireActivity().application) as App).component.plus(
            FavouriteFragmentModule()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_favourite)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        initUI()
        initStates()
        initListeners()


    }

    private fun initStates() {
        mViewModel.favouriteState.observe(viewLifecycleOwner, Observer {
            renderFavouriteState(it)
        })

        mViewModel.failure.observe(viewLifecycleOwner, Observer {
            handleFailure(it)
        })

        mViewModel.navigation.observe(viewLifecycleOwner, EventObserver {

            navController.navigate(R.id.action_favouriteFragment_to_favouriteDetailFragment,
                bundleOf(ARG_FAVOURITE to it))
        })
    }

    private fun initUI() {
        val icon = ContextCompat.getDrawable(
            this.requireContext(),
            R.drawable.ic_delete
        )
        val background = ColorDrawable(Color.RED)
        mAdapter = FavouritesAdapter(::clickOnFavourite, ::itemsSize)
        rcvFavourites.adapter = mAdapter

        icon?.let {
            val touchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(it, background, mAdapter, ::handleOnSwipe))
            touchHelper.attachToRecyclerView(rcvFavourites)
        }

        imgFavouriteImage.loadInfiniteAnimator(R.animator.heart_animator)




    }

    private fun itemsSize(size: Int) {
        if(size==0){

            lyNoItems.show()
            rcvFavourites.hide()
        }
        else{
            lyNoItems.invisible()
            rcvFavourites.show()
        }
    }

    private fun clickOnFavourite(pair: Pair<UIBusStop, UIStopFavourite>) {
        mViewModel.onClickOnFavourite(pair)
    }

    private fun initListeners() {
        swipeRefresh.setOnRefreshListener {
            mViewModel.obtainInfo()
        }
    }

    private fun handleFailure(failure: Failure?) {
        swipeRefresh.isRefreshing = false
        Log.e("Failure",failure.toString())
    }

    private fun handleOnSwipe(item: Pair<UIBusStop, UIStopFavourite>, position: Int) {
        mViewModel.onSwipedItem(item, position)


    }



    private fun renderFavouriteState(state: FavouriteViewModel.FavouriteScreenState?) {
        swipeRefresh.isRefreshing = false
        when (state) {
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
            is FavouriteViewModel.FavouriteScreenState.ShowConfirmDialogDelete -> {
                fragmentManager?.let { frm ->
                    val dialog = ConfirmDialog.newInstance(state.busData, state.position)
                    dialog.mListener = this
                    dialog.show(frm, "dialog")
                }
            }
            is FavouriteViewModel.FavouriteScreenState.ShowItemDeleted -> {
                Toast.makeText(
                    context,
                    context?.getString(R.string.deleted_item, state.busData.first.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.obtainInfo()

    }

    override fun onConfirm(item: Pair<UIBusStop, UIStopFavourite>) {
        mViewModel.deleteItem(item)
    }

    override fun onCancel(item: Pair<UIBusStop, UIStopFavourite>, position: Int) {
        mAdapter.addItem(item, position)
    }

}
