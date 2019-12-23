package com.developer.ivan.easymadbus.domain.uc

import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.UseCase
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Token
import com.developer.ivan.easymadbus.domain.repository.IBusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBusStops(private val repository: IBusRepository) :
    UseCase<GetBusStops.Params, List<BusStop>>() {
    class Params(val accessToken: Token)

    override suspend fun body(param: Params): Either<Failure, List<BusStop>> =
        repository.busStops(param.accessToken.accessToken)

}