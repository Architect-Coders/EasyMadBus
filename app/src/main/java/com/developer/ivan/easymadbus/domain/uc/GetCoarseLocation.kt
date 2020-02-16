package com.developer.ivan.easymadbus.domain.uc

import android.location.Location
import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.UseCase
import com.developer.ivan.easymadbus.framework.PlayServicesLocationDataSource
import com.google.android.gms.location.LocationCallback

class GetCoarseLocation(val dataSource: PlayServicesLocationDataSource) : UseCase<Unit,Location?>() {
    override suspend fun body(param: Unit): Either<Failure, Location?> = Either.Right(dataSource.findLastLocation())

}