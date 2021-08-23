package com.delet_dis.elementarylauncher.domain.repositories

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class AlarmsRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    var nextAlarm: Long? =
        alarmManager.nextAlarmClock?.triggerTime

    var isAlarmEnabled: Boolean = nextAlarm != null
}
