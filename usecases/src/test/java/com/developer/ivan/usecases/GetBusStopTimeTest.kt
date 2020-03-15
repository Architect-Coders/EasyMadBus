package com.developer.ivan.usecases
import com.developer.ivan.data.repository.BusRepository
import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.testshared.arrivesMock
import com.developer.ivan.testshared.tokenMock
import com.nhaarman.mockitokotlin2.verify
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

    private lateinit var getBusStopTime: GetBusStopTime

    @Before
    fun setUp() {
        getBusStopTime = GetBusStopTime(repository)
    }

    @Test
    fun `getBusStopTime always calls repository`(){
        runBlocking {
            val stopTimeParam = GetBusStopTime.Params(accessToken = tokenMock,busStop = "1")

            getBusStopTime.execute(stopTimeParam)

            verify(repository).stopTimeLines(tokenMock.accessToken,"1")
        }


    }


}