package com.developer.ivan.easymadbus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.ivan.easymadbus.data.db.dao.BusStopDao
import com.developer.ivan.easymadbus.data.db.dao.StopFavouriteDao
import com.developer.ivan.easymadbus.data.db.dao.TokenDao
import com.developer.ivan.easymadbus.data.db.models.*

@Database(
    entities = [DBBusStop::class, DBToken::class, DBStopFavourite::class, DBLine::class, DBBusStopLineCrossRef::class],
    version = 4
)
abstract class Database : RoomDatabase() {

    abstract fun busStopDao(): BusStopDao
    abstract fun tokenDao(): TokenDao
    abstract fun stopFavourite(): StopFavouriteDao

}