package com.delet_dis.elementarylauncher.domain.extensions

import com.delet_dis.elementarylauncher.data.models.LayoutType

fun findLayoutType(numberOfRows: Int): LayoutType =
    LayoutType.values().find { layoutType ->
        layoutType.numberOfRows == numberOfRows
    } ?: LayoutType.TWO_BY_THREE
