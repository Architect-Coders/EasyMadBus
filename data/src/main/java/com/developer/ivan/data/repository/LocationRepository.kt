package com.developer.ivan.data.repository

import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Locate

interface ILocationRepository {

    suspend fun findLastLocation() : Either<Failure, Locate>
    suspend fun findFineLocation() : Either<Failure, Locate>

}
class LocationRepository(private val locationDataSource: LocationDataSource) : ILocationRepository {



    override suspend fun findLastLocation(): Either<Failure, Locate> =
        locationDataSource.findLastLocation()

    override suspend fun findFineLocation(): Either<Failure, Locate> = locationDataSource.findLocationUpdates()
}