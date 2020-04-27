package com.developer.ivan.easymadbus.presentation.favourites.detail.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.hide
import com.developer.ivan.easymadbus.presentation.map.customviews.LineCustomView
import com.developer.ivan.easymadbus.presentation.map.customviews.TimeDistanceCustomView
import com.developer.ivan.easymadbus.presentation.models.UILine
import kotlinx.android.synthetic.main.layout_info_line_detail.view.*

class LineDetailCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {

        LayoutInflater.from(context).inflate(R.layout.layout_info_line_detail, this, true)

    }

    fun getDirection(headerA: String, headerB: String, direction: String): Pair<String, String> =
        when (direction.toLowerCase()) {
            "a" -> Pair(headerA,headerB)
            else -> Pair(headerB,headerA)
        }

    fun setLineDetail(line: UILine) {

        txtLine.text = context.getString(R.string.line_n, line.label)

        val directionResult = getDirection(line.headerA,line.headerB,line.direction)
        directions.setDirections(directionResult.first,directionResult.second)


        if (line.arrives.size >= 2) {
            lyArriveContainer.addView(
                TimeDistanceCustomView(
                    context
                ).apply {
                setTimeDistance(
                    line.arrives[0].estimateArrive / LineCustomView.MINUTES,
                    line.arrives[1].estimateArrive / LineCustomView.MINUTES
                )
            })
        } else {
            labelFirstBus.hide()
            labelSecondBus.hide()
        }

        txtFrequency.text = context.getString(R.string.estimated_time,line.minFreq,line.maxFreq)

    }
}
