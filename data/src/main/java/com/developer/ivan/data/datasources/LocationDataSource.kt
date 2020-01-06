package com.developer.ivan.data.datasources

import com.developer.ivan.domain.Locate

interface LocationDataSource {
    suspend fun findLastLocation(): Locate?
    suspend fun findLocationUpdates(): Locate?
    fun removeLocationUpdates()
}

interface LocationCallbackWrapper{

}