package com.developer.ivan.easymadbus.presentation.favourites.detail.customviews

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class TileDrawable(drawable: Drawable, tileModeX: Shader.TileMode, titleModeY: Shader.TileMode) : Drawable() {

    private val paint: Paint

    init {
        paint = Paint().apply {
            shader = BitmapShader(getBitmap(drawable), tileModeX, titleModeY)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        drawable.setBounds(0, drawable.intrinsicHeight/2, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(c)
        return bmp
    }

}
