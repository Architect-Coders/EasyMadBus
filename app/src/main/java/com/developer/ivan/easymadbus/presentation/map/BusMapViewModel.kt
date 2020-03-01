package com.developer.ivan.easymadbus.presentation.map

import android.Manifest
import androidx.lifecycle.*
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.usecases.*
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.*

class BusMapViewModel(
    private val busStops: GetBusStops, accessToken: GetToken,
    private val stopTime: GetBusStopTime,
    private val busAndStopsFavourites: GetBusAndStopsFavourites,
    private val insertStopFavourite: InsertStopFavourite,
    private val deleteStopFavourite: DeleteStopFavourite,
    private val coarseLocation: GetCoarseLocation,
    private val fineLocation: GetFineLocation,
    private val permissionChecker: PermissionChecker
) :
    BaseViewModel(accessToken) {

    sealed class BusStopScreenState {

        object Loading : BusStopScreenState()
        object Failure : BusStopScreenState()
        class ShowBusStops(val uiBusStop: List<UIBusStop>) : BusStopScreenState()
        class ShowFusedLocation(val location: Locate) : BusStopScreenState()
        class ShowBusStopInfo(
            val markId: String,
            val busData: Pair<UIBusStop, UIStopFavourite?>,
            val arrives: List<UIArrive>
        ) : BusStopScreenState()

        class UpdateMarkerInfoWindow(val marker: Marker) : BusStopScreenState()


        object RequestCoarseLocation : BusStopScreenState()
        object RequestFineLocation : BusStopScreenState()

    }

    private val _busState = MutableLiveData<BusStopScreenState>()
    val busState: LiveData<BusStopScreenState>
        get() = _busState

    init {
        initScope()
    }

    fun updateMarkerInfo(
        marker: Marker,
        busData: Pair<UIBusStop, UIStopFavourite?>,
        arrives: List<UIArrive>
    ) {
        val mutableListStopArrives = convertToBusArrives(busData.first, arrives)

        val busDataCopy = busData.first.copy(lines = mutableListStopArrives)

        marker.tag = Pair(busDataCopy, busData.second)

        _busState.value = (BusStopScreenState.UpdateMarkerInfoWindow(marker))

    }

    fun fusedLocation() {
        if (permissionChecker.check(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            viewModelScope.launch {
                coarseLocation.execute(Unit).fold(::handleFailure, ::handleLocation)
            }
        } else {
            _busState.value = BusStopScreenState.RequestCoarseLocation
        }
    }

    fun fineLocation() {
        if (permissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION)) {

            viewModelScope.launch {
                fineLocation.execute(Unit).fold(::handleFailure, ::handleLocation)
            }
        } else {
            _busState.value = BusStopScreenState.RequestFineLocation
        }
    }

    private suspend fun handleLocation(location: Locate?) {
        if (location != null)
            _busState.postValue(BusStopScreenState.ShowFusedLocation(location))
    }

    fun busStops() {
        executeWithToken { token ->

            viewModelScope.launch {
                busStops.execute(GetBusStops.Params(token)).fold(::handleFailure, ::handleBusStop)
            }
        }
    }

    fun clickInMark(markId: String, busStopId: String) {

        executeWithToken { token ->

            viewModelScope.launch {
                val deferredTime =
                    async { stopTime.execute(GetBusStopTime.Params(token, busStopId)) }
                val deferredFavourite =
                    async { busAndStopsFavourites.execute(GetBusAndStopsFavourites.Params(busStopId)) }

                val time = deferredTime.await()
                val favourite = deferredFavourite.await()

                time.fold(::handleFailure) { listArrives ->

                    favourite.fold(::handleFailure) { favourite ->

                        _busState.value = BusStopScreenState.ShowBusStopInfo(
                            markId,
                            Pair(
                                favourite.first().first.toUIBusStop(),
                                favourite.first().second?.toUIStopFavourite()
                            ),
                            listArrives.map { it.toUIArrive() })
                        Unit
                    }


                }
            }
        }
    }

    private suspend fun handleBusStop(busListDomain: List<BusStop>) {
        _busState.value = BusStopScreenState.ShowBusStops(busListDomain.map { it.toUIBusStop() })
    }

    fun clickInInfoWindow(markId: String, busData: Pair<UIBusStop, UIStopFavourite?>) {
        var deferredFavourite: Deferred<Either<Failure, StopFavourite>>
        viewModelScope.launch {

            deferredFavourite = if (busData.second != null) {
                async { deleteStopFavourite.execute(DeleteStopFavourite.Params(busData.second!!.toDomain())) }
            } else {
                async {
                    insertStopFavourite.execute(
                        InsertStopFavourite.Params(
                            name = busData.first.name,
                            busStopCode = busData.first.node
                        )
                    )
                }
            }

            deferredFavourite.await().fold(::handleFailure) {
                val busUI =
                    busData.copy(second = if (busData.second != null) null else it.toUIStopFavourite())
                _busState.value = (
                        BusStopScreenState.ShowBusStopInfo(
                            markId,
                            busUI,
                            busData.first.lines.flatMap { line -> line.second })
                        )
                Unit

            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelScope()
    }
}