package com.developer.ivan.easymadbus.core

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes


fun ViewGroup.inflateFragment
            (@LayoutRes layout: Int): View = LayoutInflater.from(this.context).inflate(layout,this,false)

val String.Companion.empty: String
    get() = ""

val Int.Companion.default : Int
    get() = 0

fun View.show() = run { visibility = View.VISIBLE }
fun View.hide() = run { visibility = View.GONE }
