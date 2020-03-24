package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.flatMap

class GetBusStopTime(
    private val repository: IBusRepository,
    getAccessToken: GetToken
) :
    UseCase<GetBusStopTime.Params, List<Arrive>>(),
    IExecuteToken by IExecuteToken.ExecuteTokenImpl(getAccessToken) {
    class Params(val busStop: String)

    override suspend fun body(param: Params): Either<Failure, List<Arrive>> =
        executeWithToken().flatMap {
            repository.stopTimeLines(it.accessToken, param.busStop)
        }

}