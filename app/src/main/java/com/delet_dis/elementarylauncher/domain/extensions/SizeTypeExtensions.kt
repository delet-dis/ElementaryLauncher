package com.delet_dis.elementarylauncher.domain.extensions

import com.delet_dis.elementarylauncher.data.models.SizeType

fun findScale(searchingScale: Float): SizeType {
    return SizeType.values().find { sizeType ->
        sizeType.scaleCoefficient == searchingScale
    } ?: SizeType.MEDIUM
}
