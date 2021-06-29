package com.delet_dis.elementarylauncher.domain.extensions

import com.delet_dis.elementarylauncher.data.models.SettingsActionType

fun findSettingsAction(searchingAction: String): SettingsActionType? =
    SettingsActionType.values().find { settingsActionType ->
        settingsActionType.action == searchingAction
    }
