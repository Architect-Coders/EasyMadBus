package com.developer.ivan.data.repository

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.domain.*

interface IBusRepository {
    suspend fun login(
        email: String,
        password: String,
        apiKey: String,
        clientKey: String
    ): Either<Failure, Token>

    suspend fun busStops(accessToken: String, forceReload: Boolean = false): Either<Failure, List<BusStop>>

    suspend fun stopTimeLines(accessToken: String, busStop: String): Either<Failure, List<Arrive>>

    suspend fun favourites(id: Int? = null): Either<Failure, List<StopFavourite>>
    suspend fun favouritesAndBusStops(id: String? = null): Either<Failure, List<Pair<BusStop, StopFavourite?>>>
    suspend fun insertStopFavourite(stopFavourite: StopFavourite): Either<Failure, Unit>
    suspend fun deleteStopFavourite(stopFavourite: StopFavourite): Either<Failure, Unit>
}


class BusRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IBusRepository {
     override suspend fun login(
        email: String,
        password: String,
        apiKey: String,
        clientKey: String
    ): Either<Failure, Token> {


        val token = localDataSource.getToken()
        return if (token != null && !token.isExpired()) {
            Either.Right(token)
        } else {
            val response = remoteDataSource.getLogin(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "X-ApiKey" to apiKey,
                    "X-ClientId" to clientKey
                )
            )

            when (response) {
                is Either.Right -> {
                    localDataSource.inserToken(response.b)
                    Either.Right(response.b)
                }

                is Either.Left -> response
            }
        }
    }

    @SuppressWarnings("unchecked")
    override suspend fun busStops(
        accessToken: String,
        forceReload: Boolean
    ): Either<Failure, List<BusStop>> {


        return with(localDataSource) {

            if (getCountBusStops() > 0 && !forceReload) {
                Either.Right(getBusStops())
            } else {


                val response = remoteDataSource.getBusStops(
                    mapOf(
                        "accessToken" to accessToken
                    )
                )

                when (response) {
                    is Either.Left -> response
                    is Either.Right -> {
                        localDataSource.insertBusStops(response.b)
                        Either.Right(response.b)
                    }
                }
            }
        }
    }

    override suspend fun stopTimeLines(
        accessToken: String,
        busStop: String
    ): Either<Failure, List<Arrive>> {

//        ("Text_EstimationsRequired_YN","Y")
//        "accessToken" to accessToken

        return remoteDataSource.getArrives(
            busStop,
            mapOf(
                "accessToken" to accessToken
            ),
            mapOf(
                "Text_EstimationsRequired_YN" to "Y"
            )
        )

    }

    override suspend fun favourites(id: Int?): Either<Failure, List<StopFavourite>> =
        Either.Right(localDataSource.getFavourites(id))

    override suspend fun favouritesAndBusStops(id: String?): Either<Failure, List<Pair<BusStop, StopFavourite?>>> =
        Either.Right(localDataSource.getFavouritesAndBusStops(id))

    override suspend fun insertStopFavourite(stopFavourite: StopFavourite): Either<Failure, Unit> {
        localDataSource.updateFavourite(stopFavourite)
        return Either.Right(Unit)
    }

    override suspend fun deleteStopFavourite(stopFavourite: StopFavourite): Either<Failure, Unit> {
        localDataSource.deleteFavourite(stopFavourite)
        return Either.Right(Unit)
    }
}