package com.delet_dis.elementarylauncher.data.models

import com.delet_dis.elementarylauncher.R

enum class ActionType(val stringId: Int, val imageId: Int) {
    APP(R.string.actionPickingAppText, R.drawable.ic_app),
    CONTACT(R.string.actionPickingContactText, R.drawable.ic_contact),
    CONTACT_CALL(R.string.actionPickingContactCallText, R.drawable.ic_contact_call),
    CONTACT_SMS(R.string.actionPickingContactSMSText, R.drawable.ic_contact_sms),
    WIDGET(R.string.actionPickingWidgetText, R.drawable.ic_app_widget),
    SETTINGS_ACTION(R.string.actionPickingSettingsActionText, R.drawable.ic_settings),
    CLEAR(R.string.actionPickingClearText, R.drawable.ic_clear)
    //    SHORTCUT(R.string.actionPickingAppShortcutText, R.drawable.ic_app_shortcut, 1),
}
