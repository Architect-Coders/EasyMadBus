package com.developer.ivan.easymadbus.framework.datasource

import android.app.Application
import android.location.Location
import android.os.Looper
import com.developer.ivan.data.datasources.LocationCallbackWrapper
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Either
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

    private lateinit var locationCallback: LocationCallback


    override suspend fun findLastLocation(): Locate? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(
                        if (it.result != null) Locate(
                            it.result!!.latitude,
                            it.result!!.longitude
                        ) else null
                    )
                }
        }

    override suspend fun findLocationUpdates(): Locate? {

        return suspendCancellableCoroutine { cancelableCoroutine ->
            fusedLocationClient.requestLocationUpdates(locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        cancelableCoroutine.resume(
                            if (p0?.locations?.getOrNull(0) != null){
                                val location = p0.locations[0]
                                fusedLocationClient.removeLocationUpdates(this)
                                Locate(
                                    location.latitude,
                                    location.longitude
                                )
                            } else null
                        )
                    }
                },
                null)

        }

    }

    override fun removeLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}



