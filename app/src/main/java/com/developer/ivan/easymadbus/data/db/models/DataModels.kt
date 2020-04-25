package com.developer.ivan.easymadbus.data.db.models

import androidx.room.*
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.presentation.models.*
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
    val wifi: String
) {
    //    TODO lines
    fun toUI() = UIBusStop(node, geometry.toUI(), name, wifi)

    fun toDomain() = BusStop(node, geometry.toDomain(), name, wifi)
}

@Entity(primaryKeys = ["line","direction"])
class DBLine(
    val line: String,
    val label: String,
    val direction: String,
    val maxFreq: String,
    val minFreq: String,
    val headerA: String,
    val headerB: String
){
    fun toDomain() = Line(line, label, direction, maxFreq, minFreq, headerA, headerB)
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

@Entity(
    primaryKeys = ["busStopId","lineId","directionId"],
    indices = [
        Index("busStopId"),
        Index("lineId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DBBusStop::class,
            parentColumns = ["node"],
            childColumns = ["busStopId"]
        ),
        ForeignKey(
            entity = DBLine::class,
            parentColumns = ["line","direction"],
            childColumns = ["lineId","directionId"]
        )
    ]
)
data class DBBusStopLineCrossRef(

    val busStopId: String,
    val lineId: String,
    val directionId: String
)

@Entity
data class DBStopFavourite(
    @PrimaryKey
    val busStopId: String,
    val fname: String?
) {
    fun toUI() = UIStopFavourite(busStopId, fname)
    fun toDomain() = StopFavourite(busStopId, fname)
}


data class DBBusStopWithLines(
    @Embedded
    val busStop: DBBusStop,


    @Embedded
    val crossRef: DBBusStopLineCrossRef?,

    @Embedded
    val busLine: DBLine?

//    val busLines: List<DBLine>
) {
    fun toDomain() = BusStop(
        busStop.node,
        busStop.geometry.toDomain(),
        busStop.name,
        busStop.wifi
    )
}

data class DBBusAndStopFavourite
    (
    @Embedded
    val busStop: DBBusStop,
    @Relation(
        parentColumn = "node",
        entityColumn = "busStopId"
    )
    val dbStopFavourite: DBStopFavourite? = null
) {
    fun toUI() = Pair(busStop.toUI(), dbStopFavourite?.toUI())
    fun toDomain() = Pair(busStop.toDomain(), dbStopFavourite?.toDomain())
}

data class DBGeometry(val type: String, val latitude: Double, val longitude: Double) {
    fun toUI() = UIGeometry(type, LatLng(latitude, longitude))
    fun toDomain() = Geometry(type, listOf(longitude, latitude))
}

@Entity
data class DBToken(
    @PrimaryKey
    val id: Int,
    val accessToken: String,
    val tokenSecExpiration: Int,
    val timeStamp: Long
) {
    fun toUI() = UIToken(accessToken, tokenSecExpiration, timeStamp)
    fun toDomain() = Token(accessToken, tokenSecExpiration, timeStamp)
}

@Entity
data class DBIncident(
    @PrimaryKey
    val guid: String,
    val title: String,
    val description: String,
    val link: String,
    val rssAfectaDesde: String,
    val rssAfectaHasta: String){

    fun toDomain() = Incident(guid, title, description, link, rssAfectaDesde, rssAfectaHasta)

}