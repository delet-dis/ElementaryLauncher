package com.delet_dis.elementarylauncher.data.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.util.*


abstract class DateAndTimeChangedBroadcastReceiver : BroadcastReceiver() {

    private var date: Date = Date()

    abstract fun onTimeTick()

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        val currentDate = Date()
        if (action != null &&
            (action == Intent.ACTION_TIME_CHANGED ||
                    action == Intent.ACTION_TIMEZONE_CHANGED ||
                    action == Intent.ACTION_TIME_TICK)

        ) {
            date = currentDate
            onTimeTick()
        }
    }

    companion object {
        val intentFilter: IntentFilter
            get() {
                val intentFilter = IntentFilter()
                intentFilter.addAction(Intent.ACTION_TIME_TICK)
                intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
                intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
                return intentFilter
            }
    }
}
