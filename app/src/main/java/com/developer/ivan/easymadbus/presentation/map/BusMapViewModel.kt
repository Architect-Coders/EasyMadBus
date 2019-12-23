package com.developer.ivan.easymadbus.presentation.map

import androidx.lifecycle.*
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.uc.GetBusStops
import com.developer.ivan.easymadbus.domain.uc.GetToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusMapViewModel(private val busStops: GetBusStops, accessToken: GetToken) :
    BaseViewModel(accessToken) {
    sealed class BusStopScreenState {

        object Loading : BusStopScreenState()
        object Failure : BusStopScreenState()
        class ShowBusStops(val busStops: List<BusStop>) : BusStopScreenState()
    }

    private val _busState = MutableLiveData<BusStopScreenState>()
    val busState: LiveData<BusStopScreenState>
        get() = _busState

    init {

        initScope()

        executeWithToken { token ->

            launch {

                withContext(Dispatchers.IO){
                    busStops.execute(GetBusStops.Params(token)).either(::handleFailure,::handleBusStop)
                }
            }
        }

    }

    private fun handleBusStop(busList: List<BusStop>) {
        _busState.postValue(BusStopScreenState.ShowBusStops(busList))
    }

    override fun onCleared() {
        super.onCleared()
        cancelScope()
    }

    @Suppress("UNCHECKED_CAST")
    class BusMapViewModelFactory(private val busStops: GetBusStops, private val accessToken: GetToken) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BusMapViewModel(busStops, accessToken) as T
        }
    }
}