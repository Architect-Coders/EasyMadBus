package com.developer.ivan.data.datasources

import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.Line
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.domain.Token

interface LocalDataSource
{
    suspend fun getBusStops(): List<BusStop>
    suspend fun getBusStopWithLines(busStop: String): BusStop
    suspend fun getBusStopById(busStop: String): BusStop?
    suspend fun getCountBusStops(): Int
    suspend fun getCountLines(busStop: String): Int
    suspend fun insertBusStops(busStops: List<BusStop>)
    suspend fun insertBusStopLines(busStop: String, lines: List<Line>)
    suspend fun getToken(): Token?
    suspend fun insertToken(token: Token)
    suspend fun getFavourites(id: Int?=null): List<StopFavourite>
    suspend fun getFavouritesAndBusStops(id: String?=null): List<Pair<BusStop,StopFavourite?>>
    suspend fun updateFavourite(favourite: StopFavourite)
    suspend fun deleteFavourite(favourite: StopFavourite)
}