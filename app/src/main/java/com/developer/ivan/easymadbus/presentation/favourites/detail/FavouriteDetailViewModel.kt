package com.developer.ivan.easymadbus.presentation.favourites.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.developer.ivan.easymadbus.presentation.models.convertToBusArrives
import com.developer.ivan.easymadbus.presentation.models.toUIArrive
import com.developer.ivan.usecases.GetBusStopTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class FavouriteDetailViewModel(
    private var busData: Pair<UIBusStop, UIStopFavourite>,
    private val stopTime: GetBusStopTime,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel by BaseViewModel.BaseViewModelImpl(), ViewModel() {


    sealed class FavouriteDetailScreenState {

        object Loading : FavouriteDetailScreenState()

        class ShowBusData(
            val busData: Pair<UIBusStop, UIStopFavourite>
        ) : FavouriteDetailScreenState()

        class ShowBusLine(
            val busData: UIBusStop
        ) : FavouriteDetailScreenState()


    }


    private val _favouriteDetailState = MutableLiveData<FavouriteDetailScreenState>()
    val favouriteDetailState: LiveData<FavouriteDetailScreenState>
        get() = _favouriteDetailState


    init {
        _favouriteDetailState.value = FavouriteDetailScreenState.ShowBusData(busData)
    }


    fun obtainLineInfo() {

        _favouriteDetailState.value = FavouriteDetailScreenState.Loading


        viewModelScope.launch(dispatcher) {


            stopTime.execute(GetBusStopTime.Params(busData.first.node))
                .fold(::handleFailure) { arrives ->

                    val lines = convertToBusArrives(
                        busData.first,
                        arrives.map { it.toUIArrive() })

                    busData = busData.copy(first = busData.first.copy(lines = lines))

                    _favouriteDetailState.value =
                        FavouriteDetailScreenState.ShowBusLine(busData.first)

                    Unit

                }
        }
    }

}
