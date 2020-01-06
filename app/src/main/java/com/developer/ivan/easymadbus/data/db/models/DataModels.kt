package com.developer.ivan.easymadbus.data.db.models

import androidx.room.*
import com.developer.ivan.easymadbus.domain.models.Arrive
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Geometry
import com.developer.ivan.easymadbus.domain.models.Token
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity
@TypeConverters(LinesConverter::class)
data class DBBusStop(
    @PrimaryKey
    val node: String,
    @Embedded
    val geometry: DBGeometry,
    val name: String,
    val wifi: String,
    val lines: List<String>
){
//    TODO lines
    fun toDomain() = BusStop(node,geometry.toDomain(),name,wifi, lines.map { Pair(it, listOf<Arrive>()) })
}

class LinesConverter {
    @TypeConverter
    fun stringToLines(json: String?): List<String> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson<List<String>>(json, type)
    }

    @TypeConverter
    fun linesToString(list: List<String?>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(list, type)
    }
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

@Entity
data class DBToken(
    @PrimaryKey
    val id: Int,
    val accessToken: String,
    val tokenSecExpiration: Int,
    val timeStamp: Long
){
    fun toDomain(): Token = Token(accessToken,tokenSecExpiration,timeStamp)
}