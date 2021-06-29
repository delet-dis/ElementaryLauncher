package com.delet_dis.elementarylauncher.data.models

import android.graphics.drawable.Drawable

data class Card(
    var name: String? = null,
    var icon: Drawable? = null,
    var position: Int? = null,
    var text: String? = null,
    var isWidget: Boolean = false,
    var widgetId: Int? = null,
    var onClickAction: (() -> Unit)? = null
)
