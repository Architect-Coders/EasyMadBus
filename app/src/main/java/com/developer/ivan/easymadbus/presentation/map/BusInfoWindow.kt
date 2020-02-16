package com.developer.ivan.easymadbus.presentation.map

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.TextViewCompat
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.layout_info_bus_window.view.*

class BusInfoWindow(val application: Application) : GoogleMap.InfoWindowAdapter {
    @SuppressLint("InflateParams", "WrongConstant")
    override fun getInfoContents(p0: Marker?): View? {

        val busStopInfo = p0?.tag as? Pair<UIBusStop, UIStopFavourite?>

        return if (busStopInfo != null) {
            LayoutInflater.from(application.applicationContext)
                .inflate(R.layout.layout_info_bus_window, null).apply {

                    txtBusStopName.setAutoSizeTextTypeWithDefaults(TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
                    txtBusStopName.text = busStopInfo.first.name
                    chkFavourite.isChecked = busStopInfo.second != null

                    busStopInfo.first.lines.forEach {

                        lyLineContainer.addView(LineCustomView(context).apply { setLine(it) })
                    }


                }
        } else{
            LayoutInflater.from(application.applicationContext)
                .inflate(R.layout.layout_info_bus_loader, null)
        }

    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}