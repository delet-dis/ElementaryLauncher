package com.delet_dis.elementarylauncher.domain.extensions

import com.delet_dis.elementarylauncher.data.models.HomescreenActionType

fun findHomescreenAction(searchingAction: String): HomescreenActionType? =
    HomescreenActionType.values().find { homescreenActionType ->
        homescreenActionType.name == searchingAction
    }
