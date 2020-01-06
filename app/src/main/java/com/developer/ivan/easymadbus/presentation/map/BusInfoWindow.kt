package com.developer.ivan.easymadbus.presentation.map

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.domain.models.BusStop
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.layout_info_bus_window.view.*

class BusInfoWindow(val application: Application) : GoogleMap.InfoWindowAdapter
{
    override fun getInfoContents(p0: Marker?): View? {

        val busStopInfo = Gson().fromJson<BusStop>(p0?.snippet, object: TypeToken<BusStop>(){}.type)

        return if(busStopInfo!=null){
            LayoutInflater.from(application.applicationContext).inflate(R.layout.layout_info_bus_window,null).apply {
                txtBusStopName.text = busStopInfo.name

                busStopInfo.lines.forEach {

                    lyLineContainer.addView(LineTimeCustomView(context).apply { setTimeLine(it) })
                }
            }
        }else
            null

    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}