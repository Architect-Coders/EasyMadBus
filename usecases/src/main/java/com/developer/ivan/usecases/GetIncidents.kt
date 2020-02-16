package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.*

class GetIncidents(private val repository: IBusRepository) :
    UseCase<GetIncidents.Params, List<Incident>>() {
    override suspend fun body(param: Params): Either<Failure, List<Incident>> =
        repository.incidents(param.accessToken)

    class Params(val accessToken: String)
}

/*UseCase<Params, List<Pair(BusStop,StopFavourite)>>() {
override suspend fun body(param: Params): Either<Failure, List<StopFavourite>> =
    repository.favouritesAndBusStops(param.id)

class Params(val id: Int)*/
