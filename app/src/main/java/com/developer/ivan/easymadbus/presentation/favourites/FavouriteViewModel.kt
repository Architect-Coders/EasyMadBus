package com.developer.ivan.easymadbus.presentation.favourites

import androidx.lifecycle.*
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.*

class FavouriteViewModel(
    private val stopTime: GetBusStopTime,
    private val busAndStopsFavourites: GetBusAndStopsFavourites,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel by BaseViewModel.BaseViewModelImpl(), ViewModel() {


    sealed class FavouriteScreenState {

        object Loading : FavouriteScreenState()
        class ShowBusStopFavouriteInfo(
            val busData: List<Pair<UIBusStop, UIStopFavourite>>
        ) : FavouriteScreenState()

        class ShowBusStopFavouriteLine(
            val busData: Pair<UIBusStop, UIStopFavourite>
        ) : FavouriteScreenState()


    }

    private val _favouriteState = MutableLiveData<FavouriteScreenState>()
    val favouriteState: LiveData<FavouriteScreenState>
        get() = _favouriteState

    suspend fun getStopsTimes(
        favouritesUI: List<Pair<UIBusStop, UIStopFavourite>>,
        coroutineScope: CoroutineScope
    ): List<Deferred<Either<Failure, List<Arrive>>>> {

        val deferredList =
            mutableListOf<Deferred<Either<Failure, List<Arrive>>>>()
        favouritesUI.forEach {
            deferredList.add(coroutineScope.async {
                stopTime.execute(
                    GetBusStopTime.Params(
                        it.first.node
                    )
                )
            })
        }

        return deferredList


    }

    private fun handleShowLine(
        list: List<Pair<UIBusStop, UIStopFavourite>>,
        result: List<Arrive>
    ): Pair<UIBusStop, UIStopFavourite>? {


        val favourite = list.find {
            it.second.busStopId == result.getOrNull(0)?.stop
        }

        return if (favourite != null) {
            val busData =
                convertToBusArrives(
                    favourite.first,
                    result.map { it.toUIArrive() })

            Pair(
                favourite.first.copy(lines = busData),
                favourite.second
            )

        } else
            null

    }


    fun obtainInfo() {

        _favouriteState.value = FavouriteScreenState.Loading


        viewModelScope.launch(dispatcher) {

            busAndStopsFavourites.execute(GetBusAndStopsFavourites.Params())
                .fold(::handleFailure) { response ->
                    response.filter { it.second != null }
                        .map {
                            Pair(it.first.toUIBusStop(), it.second!!.toUIStopFavourite())
                        }.let { list ->
                            _favouriteState.value = (
                                    FavouriteScreenState.ShowBusStopFavouriteInfo(
                                        list
                                    )
                                    )

                            getStopsTimes(list, viewModelScope).awaitAll()
                                .forEach { arrives ->

                                    arrives.fold(::handleFailure) { result ->

                                        handleShowLine(list, result)?.let {
                                            _favouriteState.value =
                                                FavouriteScreenState.ShowBusStopFavouriteLine(it)
                                        }
                                        Unit
                                    }

                                }
                        }
                }


        }
    }

}