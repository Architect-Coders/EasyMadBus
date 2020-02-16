package com.developer.ivan.easymadbus.domain.repository

import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.IBaseRepository
import com.developer.ivan.easymadbus.core.empty
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.data.server.models.EntityBusStop
import com.developer.ivan.easymadbus.data.server.models.EntityToken
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Token
import com.developer.ivan.easymadbus.framework.ApiService

interface IBusRepository {
    fun login(
        email: String,
        password: String,
        apiKey: String,
        clientKey: String
    ): Either<Failure, Token>

    fun busStops(accessToken: String): Either<Failure, List<BusStop>>


    class BusRepository(
        private val apiService: ApiService,
        private val serverMapper: ServerMapper
    ) :
        IBaseRepository by IBaseRepository.BaseRepositoryImplementation(), IBusRepository {
        override fun login(
            email: String,
            password: String,
            apiKey: String,
            clientKey: String
        ): Either<Failure, Token> {

            return request(
                apiService.getLogin(
                    mapOf(
                        "email" to email,
                        "password" to password,
                        "X-ApiKey" to apiKey,
                        "X-ClientId" to clientKey
                    )
                ), { token ->

                    val result = when(val data = serverMapper.parseDataServerResponseFirst<EntityToken>(token)){
                        is Either.Left -> EntityToken.empty()
                        is Either.Right -> data.b
                    }

                    result.toDomain()

                },
                String.empty
            )

        }

        @SuppressWarnings("unchecked")
        override fun busStops(accessToken: String): Either<Failure, List<BusStop>> {


            return request(
                apiService.getBusStops(
                    mapOf(
                        "accessToken" to accessToken
                    )
                ), { token ->
                    val result = when(val data = serverMapper.parseDataServerResponse<List<EntityBusStop>>(token)){
                        is Either.Left -> listOf()
                        is Either.Right -> data.b
                    }

                    result.map { it.toDomain() }
                },
                String.empty
            )

        }

    }
}