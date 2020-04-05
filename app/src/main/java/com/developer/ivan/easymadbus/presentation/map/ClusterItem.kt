package com.developer.ivan.easymadbus.presentation.map

import android.content.Context
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


class ClusterItem(
    val context: Context?, val map: GoogleMap?,
    clusterManager: ClusterManager<UIBusStop>
) :
    DefaultClusterRenderer<UIBusStop>(context, map, clusterManager) {
    private val mClusterIconGenerator =
        IconGenerator(context)

    val drawable = context?.getDrawable(R.drawable.ic_marker)
    private val bitmap = mClusterIconGenerator.apply {
        setBackground(drawable)
        setContentPadding(6,6,6,6)
    }.makeIcon()
    private val markerDescriptor =
        BitmapDescriptorFactory.fromBitmap(bitmap)

    override fun onBeforeClusterItemRendered(
        item: UIBusStop,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(markerDescriptor)
    }



    private fun isInBounds(map: GoogleMap?,position: LatLng, latLngBounds: LatLngBounds?): Boolean? {
        return (latLngBounds ?: map?.projection?.visibleRegion?.latLngBounds)?.contains(
            position
        )
    }

    override fun onClusterItemRendered(
        clusterItem: UIBusStop,
        marker: Marker
    ) {
        super.onClusterItemRendered(clusterItem, marker)

    }



}