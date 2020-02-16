package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.StopFavourite

class InsertStopFavourite(private val repository: IBusRepository) :
    UseCase<InsertStopFavourite.Params, StopFavourite>() {
    override suspend fun body(param: Params): Either<Failure, StopFavourite> {
        val stopFavourite = StopFavourite(param.busStopCode,param.name)
        repository.insertStopFavourite(stopFavourite)
        return Either.Right(stopFavourite)
    }


    class Params(val name: String, val busStopCode: String)
}

/*UseCase<Params, List<Pair(BusStop,StopFavourite)>>() {
override suspend fun body(param: Params): Either<Failure, List<StopFavourite>> =
    repository.favouritesAndBusStops(param.id)

class Params(val id: Int)*/
