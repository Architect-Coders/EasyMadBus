package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.StopFavourite

class GetBusAndStopsFavourites(private val repository: IBusRepository) :
    UseCase<GetBusAndStopsFavourites.Params, List<Pair<BusStop, StopFavourite?>>>() {
    override suspend fun body(param: Params): Either<Failure, List<Pair<BusStop, StopFavourite?>>> =
        repository.favouritesAndBusStops(param.id)

    class Params(val id: String?=null)
}

/*UseCase<Params, List<Pair(BusStop,StopFavourite)>>() {
override suspend fun body(param: Params): Either<Failure, List<StopFavourite>> =
    repository.favouritesAndBusStops(param.id)

class Params(val id: Int)*/
