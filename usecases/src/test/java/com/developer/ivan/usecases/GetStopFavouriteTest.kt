package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.testshared.stopFavouriteMock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetStopFavouriteTest {

    @Mock
    lateinit var repository: IBusRepository

    private lateinit var getStopFavourite: GetStopFavourite

    @Before
    fun setUp() {
        getStopFavourite = GetStopFavourite(repository)
    }

    @Test
    fun `getStopFavourite always calls repository`() {
        runBlocking {
            val getFavouriteParam = GetStopFavourite.Params(stopFavouriteMock.busStopId.toInt())

            getStopFavourite.execute(getFavouriteParam)

            verify(repository).favourites(getFavouriteParam.id)
        }
    }


}