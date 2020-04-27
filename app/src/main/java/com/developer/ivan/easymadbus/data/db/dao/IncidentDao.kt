package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.ivan.easymadbus.data.db.models.DBIncident

@Dao
interface IncidentDao {

    @Query("SELECT * FROM DBIncident")
    fun getAllIncidents(): List<DBIncident>

    @Query("SELECT COUNT(guid) FROM DBIncident")
    fun getIncidentsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncidents(incidents: List<DBIncident>)

}
