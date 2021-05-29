package com.delet_dis.elementarylauncher.common.models

import com.delet_dis.elementarylauncher.R

enum class PickedFragmentProgressType(val progress: Int, val fragmentId: Int) {
    WELCOME_FRAGMENT(0, R.id.welcomeFragment),
    LAYOUT_PICK_SCREEN_FRAGMENT(20, R.id.layoutPickScreenFragment),
    INTERFACE_SCALE_PICK_FRAGMENT(40, R.id.interfaceScalePickFragment),
    ACTIONS_PICK_FRAGMENT(60, R.id.actionsPickFragment),
    SET_AS_HOMESCREEN_FRAGMENT(80, R.id.setAsHomescreenFragment),
    SETUP_DONE_FRAGMENT(100, R.id.setupDoneFragment)
}