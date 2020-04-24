package com.developer.ivan.data.repository

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.NetworkDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.domain.Either
import com.developer.ivan.testshared.busStopsMock
import com.developer.ivan.testshared.tokenMock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BusRepositoryTest {


    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var networkDataSource: NetworkDataSource

    private lateinit var transactionRepository: BusRepository


    @Before
    fun setUp() {

        transactionRepository = BusRepository(remoteDataSource,localDataSource, networkDataSource)
        whenever(networkDataSource.isConnected()).thenReturn(true)
    }


    @Test
    fun `getLogin obtain remote login response if its expired`() {
        runBlocking {

            val expiredToken = tokenMock.copy(tokenSecExpiration = 0)
            val newToken = tokenMock.copy(tokenSecExpiration = 9999)

            whenever(localDataSource.getToken()).thenReturn(expiredToken)
            whenever(remoteDataSource.getLogin(anyMap())).thenReturn(Either.Right(newToken))

            val result = transactionRepository.login("", "", "", "")

            assertEquals(Either.Right(newToken),result)
        }
    }

    @Test
    fun `getLogin obtain remote login response if its not found`() {
        runBlocking {

            val notFoundToken = null
            val newToken = tokenMock.copy(tokenSecExpiration = 9999)

            whenever(localDataSource.getToken()).thenReturn(notFoundToken)
            whenever(remoteDataSource.getLogin(anyMap())).thenReturn(Either.Right(newToken))

            val result = transactionRepository.login("", "", "", "")

            assertEquals(Either.Right(newToken),result)
        }
    }

    @Test
    fun `getLogin inserts the new token in localDataSource onSuccess`(){
        runBlocking {
            val token = tokenMock.copy(tokenSecExpiration = 9999)

            whenever(remoteDataSource.getLogin(anyMap())).thenReturn(Either.Right(token))

            transactionRepository.login("", "", "", "")

            verify(localDataSource).insertToken(token)
        }
    }

    @Test
    fun `busStops returns local data source if size greater than 0 and forceReload is false`(){
        runBlocking {

            whenever(localDataSource.getCountBusStops()).thenReturn(1) //Greater than 0
            transactionRepository.busStops(tokenMock.accessToken,false)
            verify(localDataSource).getBusStops()
        }
    }

    @Test
    fun `busStops returns remote data source if the local size is lower than 1`(){
        runBlocking {

            val remoteData = Either.Right(busStopsMock)

            whenever(localDataSource.getCountBusStops()).thenReturn(0)
            whenever(remoteDataSource.getBusStops(anyMap())).thenReturn(remoteData)

            val result = transactionRepository.busStops(tokenMock.accessToken)

            assertEquals(remoteData,result)
        }
    }

    @Test
    fun `busStops returns remote data source if forceReload param its true`(){
        runBlocking {

            val remoteData = Either.Right(busStopsMock)

            whenever(localDataSource.getCountBusStops()).thenReturn(25) //More than 0
            whenever(remoteDataSource.getBusStops(anyMap())).thenReturn(remoteData)

            val result = transactionRepository.busStops(tokenMock.accessToken, true)

            assertEquals(remoteData,result)
        }
    }








}