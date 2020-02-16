package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.location.Location
import com.developer.ivan.easymadbus.framework.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationManager(
    private val permissionChecker: PermissionChecker,
    private val application: Application
) {

    private val fusedLocationClient: FusedLocationProviderClient
            by lazy { LocationServices.getFusedLocationProviderClient(application) }

    suspend fun getLocation(): Location? {
        val success = permissionChecker.check()
        return if (success) findLastLocation() else null
    }

    private suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }
}
