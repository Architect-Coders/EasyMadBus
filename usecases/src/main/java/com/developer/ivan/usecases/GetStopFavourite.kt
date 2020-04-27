package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.*

class GetStopFavourite(private val repository: IBusRepository) :
    UseCase<GetStopFavourite.Params, List<StopFavourite>>() {
    override suspend fun body(param: Params): Either<Failure, List<StopFavourite>> =
        repository.favourites(param.id)

    class Params(val id: Int)

}