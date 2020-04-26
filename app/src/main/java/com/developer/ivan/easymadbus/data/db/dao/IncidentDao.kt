package com.developer.ivan.easymadbus.data.db.dao

import androidx.room.*
import com.developer.ivan.domain.Line
import com.developer.ivan.easymadbus.data.db.models.*

@Dao
interface IncidentDao {

    @Query("SELECT * FROM DBIncident")
    fun getAllIncidents(): List<DBIncident>

    @Query("SELECT COUNT(guid) FROM DBIncident")
    fun getIncidentsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncidents(incidents: List<DBIncident>)

}