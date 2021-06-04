package com.delet_dis.elementarylauncher.common.extensions

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable

fun Drawable.getResizedDrawable(scale: Float) =
    LayerDrawable(arrayOf(this)).also { layerDrawable ->
        layerDrawable.setLayerSize(
            0,
            (this.intrinsicWidth * scale).toInt(),
            (this.intrinsicHeight * scale).toInt()
        )
    }