package com.spiraclesoftware.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

//region Context
fun Context.drawable(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

@ColorInt
fun Context.color(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.string(@StringRes resId: Int): String {
    return getString(resId)
}

fun Context.string(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}
//endregion


//region Fragment
fun Fragment.drawable(@DrawableRes resId: Int): Drawable? {
    return requireContext().drawable(resId)
}

@ColorInt
fun Fragment.color(@ColorRes resId: Int): Int {
    return requireContext().color(resId)
}

fun Fragment.string(@StringRes resId: Int): String {
    return getString(resId)
}

fun Fragment.string(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}
//endregion