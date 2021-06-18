package com.delet_dis.elementarylauncher.domain.repositories

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AlarmsRepository @Inject constructor() {
    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    fun provideNextAlarm(@ApplicationContext context: Context): Long? =
        provideAlarmManager(context).nextAlarmClock?.triggerTime

    @Provides
    fun provideIsAlarmEnabled(@ApplicationContext context: Context): Boolean =
        provideNextAlarm(context) != null
}