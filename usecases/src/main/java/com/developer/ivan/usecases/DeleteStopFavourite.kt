package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.StopFavourite

class DeleteStopFavourite(private val repository: IBusRepository) :
    UseCase<DeleteStopFavourite.Params, StopFavourite>() {
    override suspend fun body(param: Params): Either<Failure, StopFavourite> {
        repository.deleteStopFavourite(param.stopFavourite)
        return Either.Right(param.stopFavourite)
    }


    class Params(val stopFavourite: StopFavourite)
}

/*UseCase<Params, List<Pair(BusStop,StopFavourite)>>() {
override suspend fun body(param: Params): Either<Failure, List<StopFavourite>> =
    repository.favouritesAndBusStops(param.id)

class Params(val id: Int)*/
