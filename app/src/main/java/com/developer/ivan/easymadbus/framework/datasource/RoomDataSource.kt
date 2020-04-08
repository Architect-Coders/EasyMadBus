package com.developer.ivan.easymadbus.framework.datasource

import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.domain.BusStop
import com.developer.ivan.domain.Line
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.domain.Token
import com.developer.ivan.easymadbus.data.db.Database
import com.developer.ivan.easymadbus.data.db.dao.BusStopDao
import com.developer.ivan.easymadbus.data.db.dao.StopFavouriteDao
import com.developer.ivan.easymadbus.data.db.dao.TokenDao
import com.developer.ivan.easymadbus.data.db.models.DBBusStopLineCrossRef
import com.developer.ivan.easymadbus.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: Database) : LocalDataSource {
    private val busStopDao: BusStopDao = db.busStopDao()
    private val tokenDao: TokenDao = db.tokenDao()
    private val favouriteDao: StopFavouriteDao = db.stopFavourite()

    override suspend fun getBusStops(): List<BusStop> =
        withContext(Dispatchers.IO) { busStopDao.getAllBusStops().map { it.toDomain() } }

    override suspend fun getBusStopWithLines(busStop: String): BusStop? =
        withContext(Dispatchers.IO) {
            val busStopWithLines = busStopDao.getBusStopWithLines(busStop)

            val convertedBusStop = busStopWithLines[0].busStop.toDomain()
            convertedBusStop.lines = busStopWithLines.map { it.busLine?.toDomain() }.filterNotNull()
            convertedBusStop

        }

    override suspend fun getBusStopById(busStop: String): BusStop? = withContext(Dispatchers.IO) {
        busStopDao.getBusStopById(busStop)?.toDomain()
    }


    override suspend fun getCountBusStops(): Int =
        withContext(Dispatchers.IO) { busStopDao.getBusCount() }

    override suspend fun getCountLines(busStop: String): Int =
        withContext(Dispatchers.IO) { busStopDao.getLinesCount(busStop) }

    override suspend fun insertBusStops(busStops: List<BusStop>) {
        withContext(Dispatchers.IO) {
            busStopDao.insertBusStops(busStops.map { it.toDBBusStop() })
        }
    }

    override suspend fun insertBusStopLines(busStop: String, lines: List<Line>) =
        withContext(Dispatchers.IO) {
            busStopDao.insertLines(lines.map { it.toDBLine() })

            val busStopInfo = busStopDao.getBusStopById(busStop)
            val crossReferenceList = mutableListOf<DBBusStopLineCrossRef>()

            if (busStopInfo != null) {

                lines.forEach { line ->
                    crossReferenceList += DBBusStopLineCrossRef(busStop, line.line, line.direction)
                }
                busStopDao.insertBusStopLinesCrossRef(crossReferenceList)
            }
        }

    override suspend fun getToken(): Token? {
        return withContext(Dispatchers.IO) {
            tokenDao.getToken().getOrNull(0)?.toDomain()
        }
    }

    override suspend fun insertToken(token: Token) {
        withContext(Dispatchers.IO) {
            tokenDao.inserToken(token.toDBToken())
        }
    }

    override suspend fun getFavourites(id: Int?): List<StopFavourite> =
        withContext(Dispatchers.IO) { favouriteDao.getAll().map { it.toDomain() } }

    //    TODO probably can be optimized
    override suspend fun getFavouritesAndBusStops(id: String?): List<Pair<BusStop, StopFavourite?>> {
        return withContext(Dispatchers.IO) {
            val dataWithoutLines = if (id != null) {
                val data = favouriteDao.getByIdWithBusStops(id)

                if (data != null)
                    listOf(data.toDomain())
                else
                    listOf()
            } else
                favouriteDao.getAllWithBusStops().map { it.toDomain() }

            dataWithoutLines.forEach { data ->
                val busStopWithLines = busStopDao.getBusStopWithLines(data.first.node)
                data.first.lines = busStopWithLines.map { it.busLine?.toDomain() }
                    .filterNotNull()
            }
            dataWithoutLines
        }


    }

    override suspend fun updateFavourite(favourite: StopFavourite) {
        withContext(Dispatchers.IO) {
            favouriteDao.updateFavourite(favourite.toDBStopFavourite())
        }
    }

    override suspend fun deleteFavourite(favourite: StopFavourite) {
        withContext(Dispatchers.IO) {
            favouriteDao.deleteFavourite(favourite.toDBStopFavourite())
        }
    }

}