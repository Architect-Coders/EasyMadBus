package com.developer.ivan.usecases
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.testshared.arrivesMock
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
class GetBusStopTimeTest {

    @Mock
    lateinit var repository: IBusRepository

    @Mock
    lateinit var getToken: GetToken


    private lateinit var getBusStopTime: GetBusStopTime

    @Before
    fun setUp() {
        getBusStopTime = GetBusStopTime(repository,getToken)
    }

    @Test
    fun `getBusStopTime always calls repository`(){
        runBlocking {

            val spyTime = spy(getBusStopTime)
            val tokenResult = Either.Right(tokenMock.copy(tokenSecExpiration = 99999))

            val stopTimeParam = GetBusStopTime.Params(busStop = "1")

            doReturn(tokenResult).whenever(spyTime).executeWithToken()

            spyTime.execute(stopTimeParam)

            verify(repository).stopTimeLines(tokenResult.b.accessToken,"1")
        }


    }


}