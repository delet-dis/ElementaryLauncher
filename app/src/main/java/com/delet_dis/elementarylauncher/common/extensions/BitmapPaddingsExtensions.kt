package com.delet_dis.elementarylauncher.common.extensions

import android.graphics.*


fun Bitmap.addPadding(
    left:Int = 0,
    top:Int = 0,
    right:Int = 0,
    bottom:Int = 0,
    color:Int = Color.GRAY
):Bitmap?{
    val bitmap = Bitmap.createBitmap(
        width + left + right,
        height + top + bottom,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)


    canvas.drawColor(color)

    Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRect(
            Rect(left,top,bitmap.width - right,bitmap.height - bottom),
            this
        )
    }

    Paint().apply {
        canvas.drawBitmap(
            this@addPadding,
            0f + left,
            0f + top,
            this
        )
    }

    return bitmap
}