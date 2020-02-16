package com.developer.ivan.easymadbus.data.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Geometry
import com.google.android.gms.maps.model.LatLng

@Entity
data class DBBusStop(
    @PrimaryKey
    val node: String,
    @Embedded
    val geometry: DBGeometry,
    val name: String,
    val wifi: String
){
//    TODO lines
    fun toDomain() = BusStop(node,geometry.toDomain(),name,wifi, listOf(),false)
}

/*@Entity(
    foreignKeys = [ForeignKey(
        entity = DBBusStop::class,
        parentColumns = arrayOf("node"),
        childColumns = arrayOf("busStopId"),
        onDelete = CASCADE
    )]
)
data class DBFavouriteBusStop(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val busStopId: String
)*/

data class DBGeometry(val type: String, val latitude: Double, val longitude: Double)
{
    fun toDomain() = Geometry(type, LatLng(latitude,longitude))
}