package com.delet_dis.elementarylauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.delet_dis.elementarylauncher.data.database.daos.*
import com.delet_dis.elementarylauncher.data.database.entities.*

@Database(
    entities = [App::class,
        Widget::class,
        Contact::class,
        ContactCall::class,
        ContactSMS::class,
        SettingsAction::class],
    version = 1,
    exportSchema = false
)
abstract class ShortcutsDatabase : RoomDatabase() {
    abstract fun appDao(): AppDAO
    abstract fun contactCallDao(): ContactCallDAO
    abstract fun contactSmsDao(): ContactSMSDAO
    abstract fun contactDao(): ContactDAO
    abstract fun settingsActionDao(): SettingsActionDAO
    abstract fun widgetDao(): WidgetDAO
}
