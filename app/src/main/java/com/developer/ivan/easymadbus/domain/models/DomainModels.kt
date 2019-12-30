package com.developer.ivan.easymadbus.domain.models

import android.os.Parcelable
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.server.models.EntityGeometry
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Token(val accessToken: String, val tokenSecExpiration: Int) : Parcelable

@Parcelize
data class BusStop(
    val node: String,
    val geometry: Geometry,
    val name: String,
    val wifi: String,
    val lines: List<String>,
    val favourite: Boolean
) : Parcelable, ClusterItem {
    override fun getSnippet(): String = name

    override fun getTitle(): String = name

    override fun getPosition(): LatLng  = geometry.coordinates

    fun toDb(): DBBusStop = DBBusStop(node,geometry.toDb(),name,wifi)
}

@Parcelize
data class Geometry(val type: String, val coordinates: LatLng) : Parcelable
{
    fun toDb(): DBGeometry = DBGeometry(type, coordinates.latitude, coordinates.longitude)
}