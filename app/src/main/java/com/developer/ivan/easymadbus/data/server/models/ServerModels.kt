package com.developer.ivan.easymadbus.data.server.models

import com.developer.ivan.easymadbus.core.default
import com.developer.ivan.easymadbus.core.empty
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.developer.ivan.easymadbus.domain.models.Geometry
import com.developer.ivan.easymadbus.domain.models.Token
import com.google.android.gms.maps.model.LatLng

interface ServerModel


class EntityRequest<T>(val code: String, val description: String, val dateTime: String, val data: T)
class EntityToken(private val accessToken: String, private val tokenSecExpiration: Int) :
    ServerModel {
    companion object {
        fun empty() = EntityToken(String.empty, Int.default)
    }

    fun toDomain() = Token(accessToken, tokenSecExpiration)
}


class EntityGeometry(val type: String,val coordinates: List<Double>): ServerModel{
    companion object {
        fun empty() = EntityGeometry(String.empty, listOf())
    }

    fun toDomain() = Geometry(type, LatLng(coordinates[1],coordinates[0]))
}
class EntityBusStop(
    private val node: String,
    private val geometry: EntityGeometry,
    val name: String,
    val wifi: String,
    val lines: List<String>
): ServerModel{

    companion object {
        fun empty() = EntityBusStop(String.empty,EntityGeometry.empty(),String.empty,String.empty,
            listOf())
    }

    fun toDomain() = BusStop(node,geometry.toDomain(),name,wifi,lines,false)
}