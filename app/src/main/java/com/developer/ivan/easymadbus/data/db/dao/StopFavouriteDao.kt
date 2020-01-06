package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.*
import com.developer.ivan.easymadbus.data.db.models.DBBusAndStopFavourite
import com.developer.ivan.easymadbus.data.db.models.DBStopFavourite

@Dao
interface StopFavouriteDao {

    @Query("SELECT * FROM DBBusStop INNER JOIN DBStopFavourite WHERE DBBusStop.node = DBStopFavourite.busStopId")
    fun getAllWithBusStops(): List<DBBusAndStopFavourite>

    @Query("SELECT * FROM DBBusStop LEFT JOIN DBStopFavourite WHERE DBBusStop.node=:id")
    fun getByIdWithBusStops(id: String): List<DBBusAndStopFavourite>

    @Query("SELECT * FROM DBStopFavourite")
    fun getAll(): List<DBStopFavourite>

    @Query("SELECT COUNT(busStopId) FROM DBStopFavourite")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavourite(busStop: DBStopFavourite)

    @Delete
    fun deleteFavourite(busStop: DBStopFavourite)
}

/*
@Dao
interface BusStopFavouriteDao{

    @Query("SELECT * FROM DBFavouriteBusStop")
    fun getFavourites() : List<BusStop>
}*/
