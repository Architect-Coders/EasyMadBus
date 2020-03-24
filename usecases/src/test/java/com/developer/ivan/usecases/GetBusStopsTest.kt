package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Constants
import com.developer.ivan.domain.Either
import com.developer.ivan.testshared.tokenMock
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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

    @Mock
    lateinit var getToken: GetToken


    private lateinit var getBusAndStopsFavourites: GetBusStops


    @Before
    fun setup(){
        getBusAndStopsFavourites = GetBusStops(repository,getToken)
    }

    @Test
    fun `getBusStops always calls repository`() {
        runBlocking {

            val getBusStopsSpy = spy(getBusAndStopsFavourites)
            val tokenResult = Either.Right(tokenMock.copy(tokenSecExpiration = 99999))

            doReturn(Either.Right(tokenMock)).whenever(getBusStopsSpy).executeWithToken()
            getBusStopsSpy.execute(GetBusStops.Params())

            verify(repository).busStops(tokenResult.b.accessToken)
        }
    }
}