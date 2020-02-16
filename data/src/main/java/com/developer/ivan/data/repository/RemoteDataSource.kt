package com.developer.ivan.data.repository

import com.developer.ivan.domain.*


interface RemoteDataSource {
    suspend fun getLogin(headers: Map<String, String>): Either<Failure, Token>
    suspend fun getBusStops(headers: Map<String, String>): Either<Failure, List<BusStop>>
    suspend fun getArrives(
        busStop: String,
        headers: Map<String, String>,
        body: Map<String, String>
    ): Either<Failure, List<Arrive>>
    suspend fun getIncidents(
        headers: Map<String, String>
    ): Either<Failure, List<Incident>>

}