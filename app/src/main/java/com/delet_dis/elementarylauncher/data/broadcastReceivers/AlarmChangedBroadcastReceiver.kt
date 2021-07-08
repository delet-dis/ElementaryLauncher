package com.delet_dis.elementarylauncher.data.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * Broadcast receiver used to observe alarm status.
 */
abstract class AlarmChangedBroadcastReceiver : BroadcastReceiver() {

    abstract fun onAlarmChanged()

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (action != null && action == "android.app.action.NEXT_ALARM_CLOCK_CHANGED") {
            onAlarmChanged()
        }
    }

    companion object {
        val intentFilter: IntentFilter
            get() {
                val intentFilter = IntentFilter()
                intentFilter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED")
                return intentFilter
            }
    }
}
