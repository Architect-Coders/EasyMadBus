package com.developer.ivan.easymadbus.framework.datasource

import android.app.Application
import android.location.Location
import android.os.Looper
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Constants
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Locate
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(
    application: Application
) : LocationDataSource {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationRequest: LocationRequest =
        LocationRequest.create()
            .setInterval(Constants.Location.INTERVAL)
            .setFastestInterval(Constants.Location.FAST_INTERVAL)

    override suspend fun findLastLocation(): Either<Failure, Locate> =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(
                        if (it.result != null) Either.Right(
                            Locate(
                                it.result!!.latitude,
                                it.result!!.longitude
                            )
                        ) else Either.Left(Failure.NullResult)
                    )
                }
        }

    override suspend fun findLocationUpdates(): Either<Failure, Locate> {

        return suspendCancellableCoroutine { cancelableCoroutine ->
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        cancelableCoroutine.resume(
                            if (p0?.locations?.getOrNull(0) != null) {
                                val location = p0.locations[0]
                                fusedLocationClient.removeLocationUpdates(this)
                                Either.Right(
                                    Locate(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                            } else Either.Left(Failure.NullResult)
                        )
                    }
                },
                null
            )

        }

    }

}



