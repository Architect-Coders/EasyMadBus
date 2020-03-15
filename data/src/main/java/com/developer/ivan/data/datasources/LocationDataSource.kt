package com.developer.ivan.data.datasources

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Locate

interface LocationDataSource {
    suspend fun findLastLocation(): Either<Failure,Locate>
    suspend fun findLocationUpdates(): Either<Failure,Locate>
}