package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.empty
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetTokenTest {

    @Mock
    lateinit var repository: IBusRepository

    private lateinit var getToken: GetToken

    @Before
    fun setUp() {
        getToken = GetToken(repository)
    }

    @Test
    fun `getStopFavourite always calls repository`() {
        runBlocking {
            val getTokenParam =
                GetToken.Params(String.empty, String.empty, String.empty, String.empty)

            getToken.execute(getTokenParam)

            verify(repository).login(
                getTokenParam.email,
                getTokenParam.password,
                getTokenParam.apiKey,
                getTokenParam.clientKey
            )
        }
    }


}