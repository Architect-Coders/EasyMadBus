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
class InsertStopFavouriteTest {

    @Mock
    lateinit var repository: IBusRepository

    private lateinit var insertStopFavourite: InsertStopFavourite

    @Before
    fun setUp() {
        insertStopFavourite = InsertStopFavourite(repository)
    }

    @Test
    fun `getStopFavourite always calls repository`() {
        runBlocking {
            val busFavouriteParam =
                InsertStopFavourite.Params(stopFavouriteMock)

            insertStopFavourite.execute(busFavouriteParam)

            verify(repository).insertStopFavourite(busFavouriteParam.stopFavourite)
        }
    }


}