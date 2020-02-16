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
                    (serverMapper.parseDataServerResponseFirst<EntityToken>(token).either({ EntityToken.empty() },
                        { it }) as EntityToken).toDomain()
                },
                String.empty
            )

        }

        @SuppressWarnings("unchecked")
        override fun busStops(accessToken: String): Either<Failure, List<BusStop>> {


            return request(
                apiService.getLogin(
                    mapOf(
                        "accessToken" to accessToken
                    )
                ), { token ->
                    (serverMapper.parseDataServerResponse<List<EntityBusStop>>(token).either({ listOf<EntityBusStop>() },
                        { it }) as List<EntityBusStop>).map { it.toDomain() }
                },
                String.empty
            )

        }

    }
}