package com.developer.ivan.easymadbus.data.server.models

import com.developer.ivan.domain.*
import com.google.gson.annotations.SerializedName

interface ServerModel


class EntityRequest<T>(val code: String, val description: String, val dateTime: String, val data: T)
class EntityToken(private val accessToken: String, private val tokenSecExpiration: Int) :
    ServerModel {
    companion object {
        fun empty() = EntityToken(String.empty, Int.default)
    }

    fun toDomain() = Token(accessToken, tokenSecExpiration, System.currentTimeMillis())
}


class EntityGeometry(val type: String, val coordinates: List<Double>) : ServerModel {
    companion object {
        fun empty() = EntityGeometry(String.empty, listOf())
    }

    fun toDomain() = Geometry(type, coordinates)
}

class EntityArrive(
    private val line: String,
    private val stop: String,
    private val estimateArrive: Int,
    @SerializedName("DistanceBus")
    val distanceBus: Int
) : ServerModel {

    companion object {
        fun empty() = EntityArrive(String.empty, String.empty, Int.default, Int.default)
    }

    fun toDomain() = Arrive(line, stop, estimateArrive, distanceBus, System.currentTimeMillis())
}

class EntityLine(
    private val line: String,
    private val label: String,
    private val direction: String,
    private val maxFreq: String,
    private val minFreq: String,
    private val headerA: String,
    private val headerB: String
) : ServerModel {

    fun toDomain() = Line(line, label, direction, maxFreq, minFreq, headerA, headerB)
}

class EntityBusStop(
    private val node: String,
    private val geometry: EntityGeometry,
    val name: String,
    val wifi: String,
    val lines: List<String>
) : ServerModel {

    companion object {
        fun empty() = EntityBusStop(
            String.empty, EntityGeometry.empty(), String.empty, String.empty,
            listOf()
        )
    }

/*
    fun toDomain() = BusStop(
        node,
        geometry.toDomain(),
        name,
        wifi,
        lines.distinctBy { it.split("/")[0] }.map { Pair(it.split("/")[0].toInt().toString(), listOf<Arrive>()) })
*/

    fun toDomain() = BusStop(
        node,
        geometry.toDomain(),
        name,
        wifi
    )
}

data class EntityIncident(
    val guid: String,
    val title: String,
    val description: String,
    val link: String,
    val rssAfectaDesde: String,
    val rssAfectaHasta: String
) : ServerModel {

    companion object {
        fun empty() = EntityIncident(
            String.empty,String.empty, String.empty, String.empty, String.empty, String.empty
        )
    }

    fun toDomain() = Incident(
        guid,
        title,
        description,
        link,
        rssAfectaDesde,
        rssAfectaHasta
    )


}
