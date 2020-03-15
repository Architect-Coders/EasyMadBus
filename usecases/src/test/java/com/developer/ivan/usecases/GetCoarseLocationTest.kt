package com.developer.ivan.usecases

import com.developer.ivan.data.repository.ILocationRepository
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCoarseLocationTest {


    @Mock
    lateinit var repository: ILocationRepository

    private lateinit var getCoarseLocation: GetCoarseLocation

    @Before
    fun setUp() {
        getCoarseLocation = GetCoarseLocation(repository)
    }

    @Test
    fun `getCoarseLocation always calls repository`() {
        runBlocking {

            getCoarseLocation.execute(Unit)

            verify(repository).findLastLocation()

        }
    }
}