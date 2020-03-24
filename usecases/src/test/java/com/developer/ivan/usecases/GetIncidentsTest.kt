package com.developer.ivan.usecases
import com.developer.ivan.data.repository.IBusRepository
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
class GetIncidentsTest {

    @Mock
    lateinit var repository: IBusRepository

    @Mock
    lateinit var getToken: GetToken

    private lateinit var getIncidents: GetIncidents

    @Before
    fun setUp() {
        getIncidents = GetIncidents(repository,getToken)
    }

    @Test
    fun `getIncidents always calls repository`(){
        runBlocking {
            val getIncidentsParam = GetIncidents.Params()
            val getIncidentsSpy = spy(getIncidents)
            val tokenResult = Either.Right(tokenMock.copy(tokenSecExpiration = 99999))

            doReturn(tokenResult).whenever(getIncidentsSpy).executeWithToken()

            getIncidentsSpy.execute(getIncidentsParam)

            verify(repository).incidents(tokenResult.b.accessToken)
        }


    }


}