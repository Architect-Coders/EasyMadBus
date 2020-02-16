package com.developer.ivan.easymadbus.domain.repository

import android.app.Application
import com.developer.ivan.easymadbus.App
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

    fun busStops(accessToken: String, forceReload: Boolean = false): Either<Failure, List<BusStop>>


    class BusRepository(
        private val apiService: ApiService,
        private val serverMapper: ServerMapper,
        private val application: App
    ) :
        IBaseRepository by IBaseRepository.BaseRepositoryImplementation(), IBusRepository {
        override fun login(
            email: String,
            password: String,
            apiKey: String,
            clientKey: String
        ): Either<Failure, Token> {


            return with(application.database.tokenDao())
            {
                val token = getToken().getOrNull(0)?.toDomain()
                if(token!=null && !token.isExpired()){
                    Either.Right(token)
                }else
                {
                    request(
                        apiService.getLogin(
                            mapOf(
                                "email" to email,
                                "password" to password,
                                "X-ApiKey" to apiKey,
                                "X-ClientId" to clientKey
                            )
                        ), { token ->

                            val result = when (val data =
                                serverMapper.parseDataServerResponseFirst<EntityToken>(token)) {
                                is Either.Left -> EntityToken.empty()
                                is Either.Right -> data.b
                            }.toDomain()

                            inserToken(result.toDb())
                            result

                        },
                        String.empty
                    )

                }
            }
        }

        @SuppressWarnings("unchecked")
        override fun busStops(
            accessToken: String,
            forceReload: Boolean
        ): Either<Failure, List<BusStop>> {


            return with(application.database.busStopDao()) {

                if (getCount() > 0 && !forceReload) {
                    Either.Right(getAll().map { it.toDomain() })
                } else {
                    request(
                        apiService.getBusStops(
                            mapOf(
                                "accessToken" to accessToken
                            )
                        ), { token ->
                            val result = when (val data =
                                serverMapper.parseDataServerResponse<List<EntityBusStop>>(token)) {
                                is Either.Left -> listOf()
                                is Either.Right -> data.b
                            }

                            val domain = result.map { it.toDomain() }

                            insertBusStops(domain.map { it.toDb() })
                            domain

                        },
                        String.empty
                    )
                }


            }

        }

    }
}