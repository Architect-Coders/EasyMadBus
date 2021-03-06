package com.developer.ivan.usecases

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.data.repository.ILocationRepository
import com.developer.ivan.domain.Locate

class GetCoarseLocation(private val repository: ILocationRepository) : UseCase<Unit, Locate?>() {

    object NoLocation: Failure.FailureAbstract()


    override suspend fun body(param: Unit): Either<Failure, Locate?> = repository.findLastLocation()

}