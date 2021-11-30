package com.delet_dis.elementarylauncher.data.models

import com.delet_dis.elementarylauncher.R

enum class HomescreenActionType(
    val stringId: Int,
    val imageId: Int,
    val isOnboardingPart: Boolean
) {
    APPS_LIST(
        R.string.actionPickingAppsListPickText,
        R.drawable.ic_app,
        true
    ),
    ACTIONS_PICK(
        R.string.actionPickingActionsPickText,
        R.drawable.ic_app,
        true
    ),
    INTERFACE_SCALE_PICK(
        R.string.actionPickingScalePickText,
        R.drawable.ic_scale,
        true
    ),
    LAYOUT_PICK(
        R.string.actionPickingLayoutPickText,
        R.drawable.ic_app_widget,
        true
    ),
    HOMESCREEN_PICK(
        R.string.actionPickingGoToHomescreenSelectionPreferencesText,
        R.drawable.ic_home,
        false
    ),
    ABOUT_APP(
        R.string.actionPickingAboutTheAppText,
        R.drawable.ic_info,
        false
    )
}
