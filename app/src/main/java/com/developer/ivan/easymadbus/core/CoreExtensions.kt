package com.developer.ivan.easymadbus.core

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimatorRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer.ivan.easymadbus.App


fun ViewGroup.inflateFragment(@LayoutRes layout: Int): View =
    LayoutInflater.from(this.context).inflate(layout, this, false)

fun View.show() = run { visibility = View.VISIBLE }
fun View.hide() = run { visibility = View.GONE }
fun View.invisible() = run { visibility = View.INVISIBLE }
fun View.loadInfiniteAnimator(@AnimatorRes animator: Int) {
    AnimatorInflater.loadAnimator(this.context, animator).apply {
        setTarget(this@loadInfiniteAnimator)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                start()

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        start()
    }
}

val Context.app: App
    get() = applicationContext as App

fun String.removeHTML() =
    this.replace(Regex("<(.*)>.*</(.)>"), "")

fun Context.getValueInDP(value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        this.resources.displayMetrics
    ).toInt()
}

fun String.fromHTML(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }


@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this, vmFactory)[T::class.java]
}
