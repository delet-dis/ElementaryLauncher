package com.delet_dis.elementarylauncher.data.models

import com.delet_dis.elementarylauncher.R

enum class HomescreenActionType(val stringId: Int, val imageId: Int) {
    APPS_LIST(
        R.string.actionPickingAppsListPickText,
        R.drawable.ic_app
    ),
    ACTIONS_PICK(
        R.string.actionPickingActionsPickText,
        R.drawable.ic_app
    ),
    INTERFACE_SCALE_PICK(
        R.string.actionPickingScalePickText,
        R.drawable.ic_scale
    ),
    LAYOUT_PICK(
        R.string.actionPickingLayoutPickText,
        R.drawable.ic_app_widget
    ),
    HOMESCREEN_PICK(
        R.string.actionPickingGoToHomescreenSelectionPreferencesText,
        R.drawable.ic_home
    )
}
