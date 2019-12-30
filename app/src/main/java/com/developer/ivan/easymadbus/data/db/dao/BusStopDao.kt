package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.*
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.domain.models.BusStop

@Dao
interface BusStopDao{

    @Query("SELECT * FROM DBBusStop")
    fun getAll(): List<DBBusStop>

    @Query("SELECT * FROM DBBusStop WHERE node=:id")
    fun getById(id: Int): List<DBBusStop>


    @Query("SELECT COUNT(node) FROM DBBusStop")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusStops(busStop: List<DBBusStop>)

    @Update
    fun updateBusStops(listBusStop: List<DBBusStop>)
}

/*
@Dao
interface BusStopFavouriteDao{

    @Query("SELECT * FROM DBFavouriteBusStop")
    fun getFavourites() : List<BusStop>
}*/
