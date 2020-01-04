package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.*
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBToken
import com.developer.ivan.easymadbus.domain.models.BusStop

@Dao
interface TokenDao {

    @Query("SELECT * FROM DBToken")
    fun getToken(): List<DBToken>

    @Query("SELECT COUNT(id) FROM DBToken")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserToken(token: DBToken)

}
/*
@Dao
interface BusStopFavouriteDao{

    @Query("SELECT * FROM DBFavouriteBusStop")
    fun getFavourites() : List<BusStop>
}*/
