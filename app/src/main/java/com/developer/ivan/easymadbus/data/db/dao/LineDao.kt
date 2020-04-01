package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.*
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBLine

@Dao
interface LineDao{

    @Query("SELECT * FROM DBLine")
    fun getAll(): List<DBLine>

    @Query("SELECT * FROM DBLine WHERE line=:id AND direction=:direction")
    fun getById(id: Int, direction: String): List<DBLine>


    @Query("SELECT COUNT(line) FROM DBLine")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLines(busStop: List<DBLine>)

}

/*
@Dao
interface BusStopFavouriteDao{

    @Query("SELECT * FROM DBFavouriteBusStop")
    fun getFavourites() : List<BusStop>
}*/
