package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.*

class GetIncidents(
    private val repository: IBusRepository,
    getAccessToken: GetToken
) :
    UseCase<GetIncidents.Params, List<Incident>>(),
    IExecuteToken by IExecuteToken.ExecuteTokenImpl(getAccessToken) {
    override suspend fun body(param: Params): Either<Failure, List<Incident>> =
        executeWithToken().flatMap {
            repository.incidents(it.accessToken)
        }

    class Params
}