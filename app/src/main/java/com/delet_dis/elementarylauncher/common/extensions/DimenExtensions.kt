package com.delet_dis.elementarylauncher.common.extensions

import android.content.res.Resources

val Int.dpToPx
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx
    get() = this * Resources.getSystem().displayMetrics.density