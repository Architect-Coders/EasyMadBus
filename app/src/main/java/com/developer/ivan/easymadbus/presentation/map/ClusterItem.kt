package com.developer.ivan.easymadbus.presentation.map

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


class ClusterItem(
    val context: Context?, map: GoogleMap?,
    clusterManager: ClusterManager<UIBusStop>
) :
    DefaultClusterRenderer<UIBusStop>(context, map, clusterManager) {
    private val mClusterIconGenerator =
        IconGenerator(context)


    override fun onBeforeClusterItemRendered(
        item: UIBusStop,
        markerOptions: MarkerOptions
    ) {

        val drawable = context?.getDrawable(R.drawable.ic_marker)
        val bitmap = mClusterIconGenerator.apply {
            setBackground(drawable)
            setContentPadding(6,6,6,6)
        }.makeIcon()
        val markerDescriptor =
            BitmapDescriptorFactory.fromBitmap(bitmap)
        markerOptions.icon(markerDescriptor)
    }

    override fun onClusterItemRendered(
        clusterItem: UIBusStop,
        marker: Marker
    ) {
        super.onClusterItemRendered(clusterItem, marker)
    }

}