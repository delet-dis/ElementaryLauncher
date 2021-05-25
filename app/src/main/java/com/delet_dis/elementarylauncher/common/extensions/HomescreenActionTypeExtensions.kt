package com.delet_dis.elementarylauncher.common.extensions

import com.delet_dis.elementarylauncher.common.models.HomescreenActionType

fun findHomescreenAction(searchingAction: String): HomescreenActionType? {
    return HomescreenActionType.values().find {
        it.name == searchingAction
    }
}