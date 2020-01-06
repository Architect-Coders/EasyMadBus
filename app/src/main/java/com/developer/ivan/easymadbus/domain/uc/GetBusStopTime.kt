package com.developer.ivan.easymadbus.domain.uc

import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.UseCase
import com.developer.ivan.easymadbus.domain.models.Arrive
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Token
import com.developer.ivan.easymadbus.domain.repository.IBusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBusStopTime(private val repository: IBusRepository) :
    UseCase<GetBusStopTime.Params, List<Arrive>>() {
    class Params(val accessToken: Token, val busStop: String)

    override suspend fun body(param: Params): Either<Failure, List<Arrive>> =
        repository.stopTimeLines(param.accessToken.accessToken,param.busStop)

}