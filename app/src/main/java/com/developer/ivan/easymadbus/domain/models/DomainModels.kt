package com.developer.ivan.easymadbus.domain.models

import android.os.Parcelable
import com.developer.ivan.easymadbus.data.server.models.EntityGeometry
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Token(val accessToken: String, val tokenSecExpiration: Int) : Parcelable

@Parcelize
data class BusStop(
    val node: String,
    val geometry: Geometry,
    val name: String,
    val wifi: String,
    val lines: List<String>
) : Parcelable

@Parcelize
data class Geometry(val type: String, val coordinates: List<Double>) : Parcelable