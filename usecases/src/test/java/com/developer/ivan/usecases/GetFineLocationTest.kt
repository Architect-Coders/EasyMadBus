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
class GetFineLocationTest {


    @Mock
    lateinit var repository: ILocationRepository

    private lateinit var getFineLocation: GetFineLocation

    @Before
    fun setUp() {
        getFineLocation = GetFineLocation(repository)
    }

    @Test
    fun `getCoarseLocation always calls repository`() {
        runBlocking {

            getFineLocation.execute(Unit)

            verify(repository).findFineLocation()

        }
    }
}