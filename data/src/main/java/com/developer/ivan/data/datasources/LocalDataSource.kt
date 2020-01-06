package com.developer.ivan.data.datasources

import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.domain.Token

interface LocalDataSource
{
    suspend fun getBusStops(): List<BusStop>
    suspend fun getCountBusStops(): Int
    suspend fun insertBusStops(busStops: List<BusStop>)
    suspend fun getToken(): Token?
    suspend fun inserToken(token: Token)
    suspend fun getFavourites(id: Int?=null): List<StopFavourite>
    suspend fun getFavouritesAndBusStops(id: String?=null): List<Pair<BusStop,StopFavourite?>>
    suspend fun updateFavourite(favourite: StopFavourite)
    suspend fun deleteFavourite(favourite: StopFavourite)
}