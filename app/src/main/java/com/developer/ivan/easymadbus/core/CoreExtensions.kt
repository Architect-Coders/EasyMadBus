package com.developer.ivan.easymadbus.core

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.developer.ivan.easymadbus.App


fun ViewGroup.inflateFragment
            (@LayoutRes layout: Int): View = LayoutInflater.from(this.context).inflate(layout,this,false)

val String.Companion.empty: String
    get() = ""

val Int.Companion.default : Int
    get() = 0

val Long.Companion.default : Long
    get() = 0L


fun View.show() = run { visibility = View.VISIBLE }
fun View.hide() = run { visibility = View.GONE }

val Context.app: App
    get() = applicationContext as App