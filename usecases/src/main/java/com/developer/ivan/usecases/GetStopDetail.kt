package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.*

class GetStopDetail(
    private val repository: IBusRepository,
    getAccessToken: GetToken
) :
    UseCase<GetStopDetail.Params, BusStop>(),
    IExecuteToken by IExecuteToken.ExecuteTokenImpl(getAccessToken) {
    override suspend fun body(param: Params): Either<Failure, BusStop> =
        executeWithToken().flatMap {
            repository.busStopWithLines(it.accessToken,param.busStop)
        }

    class Params(val busStop: String)
}