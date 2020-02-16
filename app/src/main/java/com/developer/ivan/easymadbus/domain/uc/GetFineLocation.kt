package com.developer.ivan.easymadbus.domain.uc

import android.location.Location
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Locate
import com.developer.ivan.usecases.UseCase
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GetFineLocation(val dataSource: LocationDataSource) : UseCase<Unit, Locate?>() {

    override suspend fun body(param: Unit): Either<Failure, Locate?> {
           return Either.Right(dataSource.findLocationUpdates())
    }

}