package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.Update
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBBusStopLineCrossRef
import com.developer.ivan.easymadbus.data.db.models.DBBusStopWithLines
import com.developer.ivan.easymadbus.data.db.models.DBLine

@Dao
interface BusStopDao {

    @Query("SELECT * FROM DBBusStop")
    fun getAllBusStops(): List<DBBusStop>

    @Query("SELECT * FROM DBBusStop WHERE node=:id")
    fun getBusStopById(id: String): DBBusStop?

    @Query("SELECT * FROM DBBusStop INNER JOIN DBBusStopLineCrossRef ON node = busStopId INNER JOIN DBLine ON lineId = line AND directionId = direction")
    fun getBusStopsWithLines(): List<DBBusStopWithLines>

    @Query("SELECT * FROM DBBusStop INNER JOIN DBBusStopLineCrossRef ON node = busStopId INNER JOIN DBLine ON lineId = line AND directionId = direction WHERE  DBBusStop.node=:id")
    fun getBusStopWithLines(id: String): List<DBBusStopWithLines>

    //    For testing purposes
    @Query("SELECT * FROM DBBusStopLineCrossRef")
    fun getStopLinesCross(): List<DBBusStopLineCrossRef>


    @Query("SELECT COUNT(node) FROM DBBusStop")
    fun getBusCount(): Int

    @Query("SELECT COUNT(*) FROM DBBusStopLineCrossRef WHERE busStopId=:busStopId GROUP BY busStopId")
    fun getLinesCount(busStopId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusStops(busStop: List<DBBusStop>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusStopLinesCrossRef(crossReference: List<DBBusStopLineCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLines(lines: List<DBLine>)


    @Update
    fun updateBusStops(listBusStop: List<DBBusStop>)
}
