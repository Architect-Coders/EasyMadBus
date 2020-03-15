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
class DeleteStopFavouriteTest {


    @Mock
    private lateinit var repository: IBusRepository

    private lateinit var deleteStopFavourite: DeleteStopFavourite

    @Before
    fun setUp() {
        deleteStopFavourite = DeleteStopFavourite(repository)
    }

    @Test
    fun `deleteStop calls repository`() {
        runBlocking {
            val stopFavourite = stopFavouriteMock.copy("1")
            val stopFavouriteParam = DeleteStopFavourite.Params(stopFavourite)

            deleteStopFavourite.execute(stopFavouriteParam)

            verify(repository).deleteStopFavourite(stopFavourite)
        }
    }


}