package com.developer.ivan.easymadbus.presentation.models

import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.db.models.DBStopFavourite
import com.developer.ivan.easymadbus.data.db.models.DBToken
import com.google.android.gms.maps.model.LatLng

fun Token.toUIToken() = UIToken(accessToken, tokenSecExpiration, timeStamp)
fun Token.toDBToken() = DBToken(0,accessToken,tokenSecExpiration,timeStamp)

fun BusStop.toUIBusStop() = UIBusStop(
    node,
    geometry.toUIGeometry(),
    name,
    wifi,
    lines.map { line -> Pair(line.first, line.second.map { it.toUIArrive() }) })

fun BusStop.toDBBusStop() = DBBusStop(
    node,
    geometry.toDBGeometry(),
    name,
    wifi,
    lines.map { line -> line.first })

fun StopFavourite.toUIStopFavourite() = UIStopFavourite(
    busStopId,
    name
)

fun StopFavourite.toDBStopFavourite() = DBStopFavourite(
    busStopId,
    name
)




fun Arrive.toUIArrive() = UIArrive(line,stop,estimateArrive,distanceBus,timeStamp)

fun Geometry.toUIGeometry() = UIGeometry(type, LatLng(coordinates[1], coordinates[0]))
fun Geometry.toDBGeometry() = DBGeometry(type, coordinates[1],coordinates[0])

