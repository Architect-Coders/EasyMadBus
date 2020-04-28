package com.developer.ivan.easymadbus.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class FavouriteViewModel(
    private val stopTime: GetBusStopTime,
    private val busAndStopsFavourites: GetBusAndStopsFavourites,
    private val deleteStopFavourite: DeleteStopFavourite,
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

        class ShowConfirmDialogDelete(
            val busData: Pair<UIBusStop, UIStopFavourite>,
            val position: Int
        ) :
            FavouriteScreenState()

        class ShowItemDeleted(val busData: Pair<UIBusStop, UIStopFavourite>) :
            FavouriteScreenState()

    }


    private val _favouriteState = MutableLiveData<FavouriteScreenState>()
    val favouriteState: LiveData<FavouriteScreenState>
        get() = _favouriteState

    private val _navigationToDetail = MutableLiveData<Event<Pair<UIBusStop, UIStopFavourite>>>()
    val navigation: LiveData<Event<Pair<UIBusStop, UIStopFavourite>>>
        get() = _navigationToDetail

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

    fun onSwipedItem(item: Pair<UIBusStop, UIStopFavourite>, position: Int) {
        _favouriteState.value = FavouriteScreenState.ShowConfirmDialogDelete(item, position)

    }

    fun onClickOnFavourite(item: Pair<UIBusStop, UIStopFavourite>) {

        _navigationToDetail.value = Event(item)

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
        val uiResult: MutableList<Pair<UIBusStop, UIStopFavourite>?> = mutableListOf()


        viewModelScope.launch(dispatcher) {

            busAndStopsFavourites.execute(GetBusAndStopsFavourites.Params())
                .fold(::handleFailure) { response ->
                    response.filter { it.second != null }
                        .map {
                            Pair(it.first.toUIBusStop(), it.second!!.toUIStopFavourite())
                        }.also { list ->


                            getStopsTimes(list, viewModelScope).awaitAll()
                                .forEachIndexed { index, arrives ->

                                    arrives.fold({
                                        uiResult += list[index]
                                        Unit
                                    }) { result ->

                                        uiResult += handleShowLine(list, result)
                                        Unit
                                    }

                                }.also {
                                    _favouriteState.value = (
                                            FavouriteScreenState.ShowBusStopFavouriteInfo(
                                                uiResult.filterNotNull()
                                            )
                                            )
                                }

                        }
                }


        }
    }

    fun deleteItem(item: Pair<UIBusStop, UIStopFavourite>) {

        viewModelScope.launch {
            deleteStopFavourite.execute(DeleteStopFavourite.Params(item.second.toDomain()))
                .fold(::handleFailure) {
                    _favouriteState.value = FavouriteScreenState.ShowItemDeleted(item)
                    Unit
                }
        }
    }

}
