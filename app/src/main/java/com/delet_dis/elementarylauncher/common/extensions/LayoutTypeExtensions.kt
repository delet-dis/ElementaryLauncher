package com.delet_dis.elementarylauncher.common.extensions

import com.delet_dis.elementarylauncher.common.models.LayoutType

fun findLayoutType(numberOfRows: Int): LayoutType {
    return LayoutType.values().find { layoutType ->
        layoutType.numberOfRows == numberOfRows
    } ?: LayoutType.TWO_BY_THREE
}