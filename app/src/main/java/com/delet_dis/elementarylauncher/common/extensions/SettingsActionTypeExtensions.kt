package com.delet_dis.elementarylauncher.common.extensions

import com.delet_dis.elementarylauncher.common.models.SettingsActionType

fun findSettingsAction(searchingAction: String): SettingsActionType? {
    return SettingsActionType.values().find { settingsActionType ->
        settingsActionType.action == searchingAction
    }
}