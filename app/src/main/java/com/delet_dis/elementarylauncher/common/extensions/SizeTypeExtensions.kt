package com.delet_dis.elementarylauncher.common.extensions

import com.delet_dis.elementarylauncher.common.models.SizeType

fun findScale(searchingScale: Float): SizeType {
    return SizeType.values().find { sizeType ->
        sizeType.scaleCoefficient == searchingScale
    } ?: SizeType.MEDIUM
}