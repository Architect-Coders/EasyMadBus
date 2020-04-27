package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.*

class GetBusStops(
    private val repository: IBusRepository,
    val getAccessToken: GetToken
) :
    UseCase<GetBusStops.Params, List<BusStop>>(),
    IExecuteToken by IExecuteToken.ExecuteTokenImpl(getAccessToken) {
    class Params

    override suspend fun body(param: Params): Either<Failure, List<BusStop>> =
        executeWithToken().flatMap { repository.busStops(it.accessToken) }

}