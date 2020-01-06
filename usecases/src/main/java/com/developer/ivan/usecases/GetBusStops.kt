package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token

class GetBusStops(private val repository: IBusRepository) :
    UseCase<GetBusStops.Params, List<BusStop>>() {
    class Params(val accessToken: Token)

    override suspend fun body(param: Params): Either<Failure, List<BusStop>> =
        repository.busStops(param.accessToken.accessToken)

}