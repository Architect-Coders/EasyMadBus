package com.developer.ivan.easymadbus.framework.datasource

import com.developer.ivan.data.repository.RemoteDataSource
import com.developer.ivan.domain.empty
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.framework.ApiService
import com.developer.ivan.testshared.tokenMock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RetrofitDataSourceTest {


    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var serverMapper: ServerMapper



    private lateinit var retrofitDataSource : RemoteDataSource

    @Before
    fun setUp() {
        retrofitDataSource = RetrofitDataSource(apiService,serverMapper)
    }


    fun `getLogin gets Token if success`(){
        val tokenMock = tokenMock.copy(tokenSecExpiration = 10000)

    }

}