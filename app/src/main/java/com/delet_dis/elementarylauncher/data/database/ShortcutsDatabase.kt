package com.delet_dis.elementarylauncher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        private var INSTANCE: ShortcutsDatabase? = null

        fun getAppDatabase(context: Context): ShortcutsDatabase {
            if (INSTANCE == null) {
                synchronized(ShortcutsDatabase::class) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            ShortcutsDatabase::class.java,
                            "shortcutsDB"
                        )
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}