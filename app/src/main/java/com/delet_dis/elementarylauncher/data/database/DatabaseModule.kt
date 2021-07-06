package com.delet_dis.elementarylauncher.data.database

import android.content.Context
import androidx.room.Room
import com.delet_dis.elementarylauncher.data.database.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideAppDao(shortcutsDatabase: ShortcutsDatabase): AppDAO =
        shortcutsDatabase.appDao()

    @Provides
    fun provideContactCallDao(shortcutsDatabase: ShortcutsDatabase): ContactCallDAO =
        shortcutsDatabase.contactCallDao()

    @Provides
    fun provideContactDao(shortcutsDatabase: ShortcutsDatabase): ContactDAO =
        shortcutsDatabase.contactDao()

    @Provides
    fun provideContactSMSDao(shortcutsDatabase: ShortcutsDatabase): ContactSMSDAO =
        shortcutsDatabase.contactSmsDao()

    @Provides
    fun provideSettingsActionDao(shortcutsDatabase: ShortcutsDatabase): SettingsActionDAO =
        shortcutsDatabase.settingsActionDao()

    @Provides
    fun provideWidgetDao(shortcutsDatabase: ShortcutsDatabase): WidgetDAO =
        shortcutsDatabase.widgetDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ShortcutsDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ShortcutsDatabase::class.java,
            "shortcutsDB"
        )
            .build()
}