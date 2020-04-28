package com.developer.ivan.easymadbus.presentation.models

import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.data.db.models.*
import com.google.android.gms.maps.model.LatLng

fun Token.toDBToken() = DBToken(0, accessToken, tokenSecExpiration, timeStamp)

fun BusStop.toUIBusStop() = UIBusStop(
    node,
    geometry.toUIGeometry(),
    name,
    wifi,
    lines.map { line -> line.toUILine() })

fun BusStop.toDBBusStop() = DBBusStop(
    node,
    geometry.toDBGeometry(),
    name,
    wifi
)

fun Line.toDBLine() = DBLine(
    line, label, direction, maxFreq, minFreq, headerA, headerB
)

fun Line.toUILine() = UILine(
    line, label, direction, maxFreq, minFreq, headerA, headerB, arrives.map { it.toUIArrive() }
)

fun StopFavourite.toUIStopFavourite() = UIStopFavourite(
    busStopId,
    name
)

fun StopFavourite.toDBStopFavourite() = DBStopFavourite(
    busStopId,
    name
)

fun Incident.toUIIncident(): UIIncident {


    return UIIncident(
        guid,
        title,
        description,
        link,
        rssAfectaDesde,
        rssAfectaHasta
    )
}

fun Incident.toDBIncident() = DBIncident(
    guid,
    title,
    description,
    link,
    rssAfectaDesde,
    rssAfectaHasta
)


fun Arrive.toUIArrive() = UIArrive(line, stop, estimateArrive, distanceBus, timeStamp)

fun Geometry.toUIGeometry() = UIGeometry(type, LatLng(coordinates[1], coordinates[0]))
fun Geometry.toDBGeometry() = DBGeometry(type, coordinates[1], coordinates[0])
