package com.developer.ivan.data.repository

import com.developer.ivan.data.datasources.LocationDataSource
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.testshared.locateMock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LocationRepositoryTest {


    @Mock
    private lateinit var locationDataSource: LocationDataSource

    private lateinit var locationRepository: ILocationRepository

    @Before
    fun setUp() {
        locationRepository = LocationRepository(locationDataSource)
    }


    @Test
    fun `findLastLocation calls location data source`(){
        runBlocking {
            locationRepository.findLastLocation()

            verify(locationDataSource).findLastLocation()
        }

    }

    @Test
    fun `findFineLocation calls location data source`(){
        runBlocking {
            locationRepository.findFineLocation()

            verify(locationDataSource).findLocationUpdates()
        }

    }




}