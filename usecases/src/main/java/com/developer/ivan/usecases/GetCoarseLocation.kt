package com.developer.ivan.usecases

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Locate

class GetCoarseLocation(private val dataSource: LocationDataSource) : UseCase<Unit, Locate?>() {
    override suspend fun body(param: Unit): Either<Failure, Locate?> = Either.Right(dataSource.findLastLocation())

}