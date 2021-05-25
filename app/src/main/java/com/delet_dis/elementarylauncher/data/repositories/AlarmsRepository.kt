package com.delet_dis.elementarylauncher.data.repositories

import android.app.AlarmManager
import android.content.Context

class AlarmsRepository(val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    var nextAlarm: Long? =
        alarmManager.nextAlarmClock?.triggerTime

    var isAlarmEnabled: Boolean = nextAlarm != null
}