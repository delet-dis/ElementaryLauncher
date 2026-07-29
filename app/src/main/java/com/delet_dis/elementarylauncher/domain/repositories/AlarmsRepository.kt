package com.delet_dis.elementarylauncher.domain.repositories

import android.app.AlarmManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmsRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val nextAlarm: Long?
        get() = alarmManager.nextAlarmClock?.triggerTime

    val isAlarmEnabled: Boolean
        get() = nextAlarm != null
}
