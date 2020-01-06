package com.developer.ivan.easymadbus.framework.datasource

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.domain.Token
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.data.db.dao.BusStopDao
import com.developer.ivan.easymadbus.data.db.dao.StopFavouriteDao
import com.developer.ivan.easymadbus.data.db.dao.TokenDao
import com.developer.ivan.easymadbus.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: Database) : LocalDataSource {
    private val busStopDao: BusStopDao = db.busStopDao()
    private val tokenDao: TokenDao = db.tokenDao()
    private val favouriteDao: StopFavouriteDao = db.stopFavourite()

    override suspend fun getBusStops(): List<BusStop> =
        withContext(Dispatchers.IO) { busStopDao.getAll().map { it.toDomain() } }

    override suspend fun getCountBusStops(): Int =
        withContext(Dispatchers.IO) { busStopDao.getCount() }

    override suspend fun insertBusStops(busStops: List<BusStop>) {
        withContext(Dispatchers.IO){
            busStopDao.insertBusStops(busStops.map { it.toDBBusStop() })
        }
    }

    override suspend fun getToken(): Token? {
      return withContext(Dispatchers.IO){
          tokenDao.getToken().getOrNull(0)?.toDomain()
      }
    }

    override suspend fun inserToken(token: Token) {
        withContext(Dispatchers.IO){
            tokenDao.inserToken(token.toDBToken())
        }
    }

    override suspend fun getFavourites(id: Int?): List<StopFavourite> =
        withContext(Dispatchers.IO) {favouriteDao.getAll().map { it.toDomain() }}

    override suspend fun getFavouritesAndBusStops(id: String?): List<Pair<BusStop, StopFavourite?>> {
        return withContext(Dispatchers.IO){
             if (id != null)
                favouriteDao.getByIdWithBusStops(id).map { it.toDomain() }
            else
                favouriteDao.getAllWithBusStops().map { it.toDomain() }
        }


    }

    override suspend fun updateFavourite(favourite: StopFavourite) {
        withContext(Dispatchers.IO){
            favouriteDao.updateFavourite(favourite.toDBStopFavourite())
        }
    }

    override suspend fun deleteFavourite(favourite: StopFavourite) {
        withContext(Dispatchers.IO){
            favouriteDao.deleteFavourite(favourite.toDBStopFavourite())
        }
    }

}