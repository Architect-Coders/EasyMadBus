package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.testshared.busStopsMock
import com.developer.ivan.testshared.stopFavouriteMock
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
class GetBusAndStopsFavouritesTest {

    @Mock
    private lateinit var repository: IBusRepository

    private lateinit var getBusAndStopsFavourites: GetBusAndStopsFavourites

    @Before
    fun setUp() {
        getBusAndStopsFavourites = GetBusAndStopsFavourites(repository)
    }

    @Test
    fun `getBusAndStopsFavourites calls repository`() {
        runBlocking {
            val stopFavourite = stopFavouriteMock.copy("1")
            val stopFavouriteParam = GetBusAndStopsFavourites.Params(stopFavourite.busStopId)

            getBusAndStopsFavourites.execute(stopFavouriteParam)

            verify(repository).favouritesAndBusStops(stopFavourite.busStopId)
        }
    }

    @Test
    fun `getBusAndStopsFavourites returns a list of pair - BusStop, StopFavourite`()
    {
        runBlocking {
            val stopFavourite = stopFavouriteMock.copy("1")
            val busStop = busStopsMock[0].copy(node = "1")


            val expectedList = listOf(Pair(busStop,stopFavourite))

            val stopFavouriteParam = GetBusAndStopsFavourites.Params(stopFavourite.busStopId)


            whenever(repository.favouritesAndBusStops(stopFavourite.busStopId)).thenReturn(Either.Right(expectedList))

            val result = getBusAndStopsFavourites.execute(stopFavouriteParam)

            assertEquals(Either.Right(expectedList),result)
        }
    }



}