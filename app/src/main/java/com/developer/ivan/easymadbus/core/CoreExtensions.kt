package com.developer.ivan.easymadbus.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes


fun ViewGroup.inflateFragment(@LayoutRes layout: Int) = LayoutInflater.from(this.context).inflate(layout,this,false)