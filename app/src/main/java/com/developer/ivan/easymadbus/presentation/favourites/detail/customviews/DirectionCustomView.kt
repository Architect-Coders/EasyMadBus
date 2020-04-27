package com.developer.ivan.easymadbus.presentation.favourites.detail.customviews

import android.content.Context
import android.graphics.Shader
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.developer.ivan.domain.empty
import com.developer.ivan.easymadbus.R
import kotlinx.android.synthetic.main.layout_direction.view.*

class DirectionCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mStartDirection: String
    private var mEndDirection: String


    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DirectionCustomView,
            defStyleAttr, 0).apply {

            try {

                mStartDirection = getString(R.styleable.DirectionCustomView_startDirection) ?: String.empty
                mEndDirection = getString(R.styleable.DirectionCustomView_endDirection) ?: String.empty
            } finally {
                recycle()
            }
        }

        LayoutInflater.from(context).inflate(R.layout.layout_direction, this, true)

        setDirections(mStartDirection,mEndDirection)

    }


    fun setDirections(startDirection: String,
                      endDirection: String){

        txtStart?.text = startDirection
        txtEnd?.text = endDirection

        imgDirection?.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_direction_right)?.let {
            TileDrawable(
                it, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP
            )
        })
    }

}
