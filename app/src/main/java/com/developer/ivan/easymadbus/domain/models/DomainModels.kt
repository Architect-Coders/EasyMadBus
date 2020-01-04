package com.developer.ivan.easymadbus.domain.models

import android.os.Parcelable
import com.developer.ivan.easymadbus.core.Constants
import com.developer.ivan.easymadbus.data.db.models.DBBusStop
import com.developer.ivan.easymadbus.data.db.models.DBGeometry
import com.developer.ivan.easymadbus.data.db.models.DBToken
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class Token(val accessToken: String, val tokenSecExpiration: Int, val timeStamp: Long) : Parcelable
{
    fun isExpired() =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timeStamp) >= tokenSecExpiration

    fun toDb(): DBToken = DBToken(Constants.Token.TOKEN_ID,accessToken,tokenSecExpiration,timeStamp)

}
@Parcelize
data class BusStop(
    val node: String,
    val geometry: Geometry,
    val name: String,
    val wifi: String,
    val lines: List<String>) : Parcelable, ClusterItem {
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