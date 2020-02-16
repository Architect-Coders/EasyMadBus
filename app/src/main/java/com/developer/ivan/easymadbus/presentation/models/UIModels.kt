package com.developer.ivan.easymadbus.presentation.models

import android.os.Parcelable
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.db.models.DBToken
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@Parcelize
data class UIToken(val accessToken: String, val tokenSecExpiration: Int, val timeStamp: Long) :
    Parcelable {
    fun isExpired() =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timeStamp) >= tokenSecExpiration

    fun toDb(): DBToken =
        DBToken(Constants.Token.TOKEN_ID, accessToken, tokenSecExpiration, timeStamp)

}

@Parcelize
data class UIBusStop(
    val node: String,
    val geometry: UIGeometry,
    val name: String,
    val wifi: String,
    val lines: List<Pair<String, List<UIArrive>>>
) : Parcelable, ClusterItem {
/*
    override fun getSnippet(): String =
            lines.map { it.split("/") }.getOrNull(0)?.joinToString(", ") ?: String.empty
*/

    override fun getSnippet(): String = node
    override fun getTitle(): String = name

    override fun getPosition(): LatLng = geometry.coordinates

    fun toDb(): DBBusStop = DBBusStop(node, geometry.toDb(), name, wifi, lines.map { it.first })
    fun toDomain(): BusStop = BusStop(
        node,
        geometry.toDomain(),
        name,
        wifi,
        lines.map { Pair(it.first, it.second.map { arrive -> arrive.toDomain() }) })
}

@Parcelize
data class UIStopFavourite(
    val busStopId: String,
    val name: String?
) : Parcelable {
    companion object {
        fun empty() = UIStopFavourite(String.empty, String.empty)
    }

    fun isEmpty() = this == empty()
    fun toDomain() = StopFavourite(busStopId, name)

}

@Parcelize
data class UIArrive(
    val line: String,
    val stop: String,
    val estimateArrive: Int,
    val distanceBus: Int,
    val timeStamp: Long
) : Parcelable {
    fun toDomain(): Arrive = Arrive(line, stop, estimateArrive, distanceBus, timeStamp)

}

@Parcelize
data class UIGeometry(val type: String, val coordinates: LatLng) : Parcelable {
    fun toDb(): DBGeometry = DBGeometry(type, coordinates.latitude, coordinates.longitude)
    fun toDomain(): Geometry = Geometry(type, listOf(coordinates.latitude, coordinates.longitude))
}

@Parcelize
data class UIIncident(
    val title: String,
    val description: String,
    val link: String,
    val rssAfectaDesde: String,
    val rssAfectaHasta: String
) : Parcelable {
    fun toDomain(): Incident = Incident(
        title,
        description,
        link,
        rssAfectaDesde,
        rssAfectaHasta
    )
}


fun convertToBusArrives(busStop: UIBusStop, arrives: List<UIArrive>) =
    busStop.lines.distinctBy { it.first.split("/")[0] }.map { line ->
        Pair(
            line.first,
            (arrives.filter {
                it.line == line.first.split(
                    "/"
                )[0].toInt().toString()
            })
        )
    }