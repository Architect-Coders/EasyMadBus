package com.developer.ivan.easymadbus.presentation.models

import android.os.Parcelable
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.db.models.DBLine
import com.developer.ivan.easymadbus.data.db.models.DBToken
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
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
data class UILine(
    val line: String,
    val label: String,
    val direction: String,
    val maxFreq: String,
    val minFreq: String,
    val headerA: String,
    val headerB: String,
    var arrives: List<UIArrive> = emptyList()
) : Parcelable

@Parcelize
data class UIBusStop(
    val node: String,
    val geometry: UIGeometry,
    val name: String,
    val wifi: String,
    var lines: List<UILine> = emptyList()
) : Parcelable, ClusterItem {


    override fun getSnippet(): String = node
    override fun getTitle(): String = name

    override fun getPosition(): LatLng = geometry.coordinates

    fun toDb(): DBBusStop = DBBusStop(node, geometry.toDb(), name, wifi)
    fun toDomain(): BusStop = BusStop(
        node,
        geometry.toDomain(),
        name,
        wifi
    )
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
    val guid: String,
    val title: String,
    val description: String,
    val link: String,
    val rssAfectaDesde: String,
    val rssAfectaHasta: String
) : Parcelable {
    fun toDomain(): Incident = Incident(
        guid,
        title,
        description,
        link,
        rssAfectaDesde,
        rssAfectaHasta
    )
}


fun convertToBusArrives(busStop: UIBusStop, arrives: List<UIArrive>): List<UILine> {
    return busStop.lines.map { line ->
        line.arrives = arrives.filter { arrive ->
            arrive.line.toLowerCase(Locale.getDefault()) == line.label.toLowerCase(Locale.getDefault())
        }
        line
    }
}
