package com.developer.ivan.easymadbus.presentation.favourites.detail

import android.content.Intent
import androidx.lifecycle.*
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.core.Event
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.usecases.DeleteStopFavourite
import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import kotlinx.coroutines.*

class FavouriteDetailViewModel(
    private var busData: Pair<UIBusStop,UIStopFavourite>,
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
        ): FavouriteDetailScreenState()


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