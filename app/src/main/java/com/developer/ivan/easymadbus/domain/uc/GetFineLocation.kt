package com.developer.ivan.easymadbus.domain.uc

import android.location.Location
import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.UseCase
import com.developer.ivan.easymadbus.framework.PlayServicesLocationDataSource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GetFineLocation(val dataSource: PlayServicesLocationDataSource) : UseCase<Unit, Location?>() {

    override suspend fun body(param: Unit): Either<Failure, Location?> {
           return suspendCancellableCoroutine { cancelableCoroutine->
                Either.Right(dataSource.findLocationUpdates(object: LocationCallback(){
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        cancelableCoroutine.resume(Either.Right(p0?.locations?.getOrNull(0)))
                    }
                }))
            }
    }




}