package com.developer.ivan.easymadbus.domain.uc

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Arrive
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token
import com.developer.ivan.easymadbus.core.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBusStopTime(private val repository: IBusRepository) :
    UseCase<GetBusStopTime.Params, List<Arrive>>() {
    class Params(val accessToken: Token, val busStop: String)

    override suspend fun body(param: Params): Either<Failure, List<Arrive>> =
        repository.stopTimeLines(param.accessToken.accessToken,param.busStop)

}