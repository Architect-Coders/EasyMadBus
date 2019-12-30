package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.location.Location
import android.os.Looper
import com.developer.ivan.easymadbus.core.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application
                                     ) : LocationDataSource {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationRequest: LocationRequest =
        LocationRequest.create()
            .setInterval(Constants.Location.INTERVAL)
            .setFastestInterval(Constants.Location.FAST_INTERVAL)


    override suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    override fun findLocationUpdates(locationCallback: LocationCallback
    ){
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun removeLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


}


interface LocationDataSource {
    suspend fun findLastLocation(): Location?
    fun findLocationUpdates(locationCallback: LocationCallback)
    fun removeLocationUpdates(locationCallback: LocationCallback)
}