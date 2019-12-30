package com.developer.ivan.easymadbus.presentation.map

import android.Manifest
import android.location.Location
import androidx.lifecycle.*
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.uc.*
import com.developer.ivan.easymadbus.framework.LocationDataSource
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.easymadbus.framework.PlayServicesLocationDataSource
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusMapViewModel(
    private val busStops: GetBusStops, accessToken: GetToken,
    private val coarseLocation: GetCoarseLocation,
    private val fineLocation: LocationDataSource,
    private val permissionChecker: PermissionChecker
) :
    BaseViewModel(accessToken) {
    sealed class BusStopScreenState {

        object Loading : BusStopScreenState()
        object Failure : BusStopScreenState()
        class ShowBusStops(val busStops: List<BusStop>) : BusStopScreenState()
        class ShowFusedLocation(val location: Location) : BusStopScreenState()
        object RequestCoarseLocation : BusStopScreenState()
        object RequestFineLocation : BusStopScreenState()
    }

    private val _busState = MutableLiveData<BusStopScreenState>()
    val busState: LiveData<BusStopScreenState>
        get() = _busState

    init {

        initScope()

    }

    fun fusedLocation() {
        if (permissionChecker.check(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            launch {
                coarseLocation.execute(Unit).either(::handleFailure, ::handleLocation)
            }
        } else {
            _busState.value = BusStopScreenState.RequestCoarseLocation
        }
    }

    fun fineLocation(){
        if (permissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION)) {

            fineLocation.findLocationUpdates(object: LocationCallback(){
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    p0?.locations?.getOrNull(0)?.let {
                        _busState.value = BusStopScreenState.ShowFusedLocation(it)

                    }
                }
            })
        } else {
            _busState.value = BusStopScreenState.RequestFineLocation
        }
    }

    private fun handleLocation(location: Location?) {
        if (location != null)
            _busState.postValue(BusStopScreenState.ShowFusedLocation(location))
    }

    fun busStops() {
        executeWithToken { token ->

            launch {
                busStops.execute(GetBusStops.Params(token)).either(::handleFailure, ::handleBusStop)
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
    class BusMapViewModelFactory(
        private val busStops: GetBusStops,
        private val accessToken: GetToken,
        val location: GetCoarseLocation,
        val fineLocation: PlayServicesLocationDataSource,
        val permissionChecker: PermissionChecker
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BusMapViewModel(busStops, accessToken,location,fineLocation,permissionChecker) as T
        }
    }
}