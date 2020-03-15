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
class GetIncidentsTest {

    @Mock
    lateinit var repository: IBusRepository

    private lateinit var getIncidents: GetIncidents

    @Before
    fun setUp() {
        getIncidents = GetIncidents(repository)
    }

    @Test
    fun `getIncidents always calls repository`(){
        runBlocking {
            val getIncidentsParam = GetIncidents.Params(token = tokenMock)

            getIncidents.execute(getIncidentsParam)

            verify(repository).incidents(getIncidentsParam.token.accessToken)
        }


    }


}