package com.developer.ivan.easymadbus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.ivan.easymadbus.data.db.dao.BusStopDao
import com.developer.ivan.easymadbus.data.db.models.DBBusStop

@Database(entities = [DBBusStop::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun busStopDao(): BusStopDao
//    abstract fun busStopFavourite(): BusStopFavouriteDao

}