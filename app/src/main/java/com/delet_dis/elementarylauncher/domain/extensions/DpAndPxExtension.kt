package com.delet_dis.elementarylauncher.domain.extensions

import android.content.Context
import android.util.DisplayMetrics

fun Float.pxToDp(context: Context) =
    this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
