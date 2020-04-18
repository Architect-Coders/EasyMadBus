package com.developer.ivan.easymadbus.presentation.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.fromHTML
import com.developer.ivan.easymadbus.presentation.models.UILine
import kotlinx.android.synthetic.main.layout_estimated.view.*
import kotlinx.android.synthetic.main.layout_info_line.view.*
import kotlinx.android.synthetic.main.layout_time_distance.view.*

class LineCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mFixedSize: Int

    companion object {
        const val MINUTES = 60
    }

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LineCustomView,
            0, 0).apply {

            try {
                mFixedSize = getInt(R.styleable.LineCustomView_fixedSize, 0)
            } finally {
                recycle()
            }
        }

        val view = LayoutInflater.from(context).inflate(R.layout.layout_info_line, this, true)

        handleFixedSize(mFixedSize,view)





    }

    private fun handleFixedSize(fixedSize: Int, view: View){
        if(fixedSize>0){
            val layoutParams = view.constraintParent.layoutParams
            layoutParams.height = mFixedSize
            view.constraintParent.requestLayout()
        }
    }

    fun setLine(line: UILine) = line.apply {

        txtLine?.text = label

        if (arrives.size==2) {

            timeDistance.addView(TimeDistanceCustomView(context).apply {
                setTimeDistance(
                    arrives[0].estimateArrive / MINUTES,
                    arrives[1].estimateArrive / MINUTES
                )
            })

        }else{
            timeDistance.addView(EstimatedCustomView(context).apply {
                setTimeDistance(line.minFreq,line.maxFreq)
            })
        }
    }

    fun setFixedSize(size: Int){
        mFixedSize = size
        handleFixedSize(mFixedSize,this)
        invalidate()
        requestLayout()
    }
}

class TimeDistanceCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val kmConstant = 1000

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_time_distance, this, true)
    }

    fun setTimeDistance(timeInMinutes: Int, timeInMinutes2: Int) {


        txtTime?.text = "${timeInMinutes} min"
        txtTime2?.text = "${timeInMinutes2} min"
//        txtTime2?.text = "%.1f km".format((distanceInMeters/kmConstant.toFloat()))

    }

}

class EstimatedCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_estimated, this, true)
    }

    fun setTimeDistance(minFreq: String, maxFreq: String) {
        txtEstimated?.text = context.getString(R.string.time_error_estimated_time,minFreq,maxFreq).fromHTML()
    }

}