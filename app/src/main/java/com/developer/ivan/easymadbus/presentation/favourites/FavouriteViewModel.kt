package com.developer.ivan.easymadbus.presentation.favourites

import androidx.lifecycle.*
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.usecases.GetBusAndStopsFavourites
import com.developer.ivan.usecases.GetBusStopTime
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.*

class FavouriteViewModel(
    accessToken: GetToken,
    private val stopTime: GetBusStopTime,
    private val busAndStopsFavourites: GetBusAndStopsFavourites
) :
    BaseViewModel(accessToken) {

    init {
        initScope()
    }

    sealed class FavouriteScreenState {

        object Loading : FavouriteScreenState()
        class ShowError(val failure: Failure) : FavouriteScreenState()
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

    private suspend fun getStopsTimes(
        accessToken: Token,
        favouritesUI: List<Pair<UIBusStop, UIStopFavourite>>
    ): List<Deferred<Either<Failure, List<Arrive>>>> {
        val deferredList =
            mutableListOf<Deferred<Either<Failure, List<Arrive>>>>()
        favouritesUI.forEach {
            deferredList.add(async {
                stopTime.execute(
                    GetBusStopTime.Params(
                        accessToken,
                        it.first.node
                    )
                )
            })
        }

        return deferredList


    }


    fun obtainInfo() {

        _favouriteState.value = FavouriteScreenState.Loading


        executeWithToken { token ->
            viewModelScope.launch {

                busAndStopsFavourites.execute(GetBusAndStopsFavourites.Params())
                    .fold(fnL = ::handleFailure) { response ->
                        response.filter { it.second != null }
                            .map {
                                Pair(it.first.toUIBusStop(), it.second!!.toUIStopFavourite())
                            }.let { list ->
                                _favouriteState.value = (
                                        FavouriteScreenState.ShowBusStopFavouriteInfo(
                                            list
                                        )
                                        )

                                getStopsTimes(token,list).awaitAll().forEach { arrives ->

                                    arrives.fold(::handleFailure){result->
                                        val favourite = list.find {
                                            it.second.busStopId == result.getOrNull(0)?.stop
                                        }

                                        if (favourite != null) {
                                            val busData =
                                                convertToBusArrives(
                                                    favourite.first,
                                                    result.map { it.toUIArrive() })

                                            _favouriteState.value = (
                                                    FavouriteScreenState.ShowBusStopFavouriteLine(
                                                        Pair(
                                                            favourite.first.copy(lines = busData),
                                                            favourite.second
                                                        )
                                                    )
                                                    )
                                        }
                                    }
                                }
                            }
                    }

            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelScope()
    }

    @Suppress("UNCHECKED_CAST")
    class FavouriteViewModelFactory(
        private val accessToken: GetToken,
        private val stopTime: GetBusStopTime,
        private val busAndStopsFavourites: GetBusAndStopsFavourites
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavouriteViewModel(
                accessToken,
                stopTime,
                busAndStopsFavourites
            ) as T
        }
    }
}