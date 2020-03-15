package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.testshared.tokenMock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class GetBusStopsTest {


    @Mock
    private lateinit var repository: IBusRepository

    private lateinit var getBusAndStopsFavourites: GetBusStops


    @Before
    fun setup(){
        getBusAndStopsFavourites = GetBusStops(repository)
    }

    @Test
    fun `getBusStops always calls repository`() {
        runBlocking {
            getBusAndStopsFavourites.execute(GetBusStops.Params(tokenMock))

            verify(repository).busStops(tokenMock.accessToken)
        }
    }
}