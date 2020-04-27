package com.developer.ivan.easymadbus.presentation.map.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.developer.ivan.domain.Arrive
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.hide
import kotlinx.android.synthetic.main.layout_info_line_time.view.*

class LineTimeCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object{
        const val MINUTES = 60
    }
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_info_line_time, this, true)
    }

    fun setTimeLine(timeLine: Pair<String, List<Arrive>>) = timeLine.apply {

        txtLine?.text = timeLine.first

        if(timeLine.second.isNotEmpty()){
            progressTime.hide()
            txtTime?.text =
                timeLine.second.map { "${it.estimateArrive / MINUTES} min (${it.distanceBus} m)" }
                    .joinToString(",\n")
        }

    }

}
