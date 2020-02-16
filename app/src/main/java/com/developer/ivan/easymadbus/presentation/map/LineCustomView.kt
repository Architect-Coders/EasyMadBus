package com.developer.ivan.easymadbus.presentation.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.models.UIArrive
import kotlinx.android.synthetic.main.layout_info_line.view.*
import kotlinx.android.synthetic.main.layout_time_distance.view.*

class LineCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        const val MINUTES = 60
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_info_line, this, true)
    }

    fun setLine(timeLine: Pair<String, List<UIArrive>>) = timeLine.apply {

        txtLine?.text = context.getString(R.string.line_n, timeLine.first)

        if (timeLine.second.isNotEmpty()) {

            timeLine.second.forEach {
                timeDistance.addView(TimeDistanceCustomView(context).apply {
                    setTimeDistance(
                        it.estimateArrive / MINUTES,
                        it.distanceBus
                    )
                })
            }

        }

    }

}

class TimeDistanceCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_time_distance, this, true)
    }

    fun setTimeDistance(timeInMinutes: Int, distanceInMeters: Int) {


        txtTime?.text = "${timeInMinutes} min"
        txtDistance?.text = "${distanceInMeters} m"

    }

}