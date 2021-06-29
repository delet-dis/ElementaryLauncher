package com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.delet_dis.elementarylauncher.data.broadcastReceivers.AlarmChangedBroadcastReceiver
import com.delet_dis.elementarylauncher.data.broadcastReceivers.DateAndTimeChangedBroadcastReceiver
import com.delet_dis.elementarylauncher.domain.helpers.formatToDigitalClock
import com.delet_dis.elementarylauncher.domain.helpers.getCurrentDate
import com.delet_dis.elementarylauncher.domain.helpers.getCurrentTime
import com.delet_dis.elementarylauncher.domain.repositories.AlarmsRepository

class ClockViewViewModel(application: Application) : AndroidViewModel(application) {

    private val _dateLiveData = MutableLiveData(getCurrentDate())
    val dateLiveData: LiveData<String>
        get() = _dateLiveData

    private val _timeLiveData = MutableLiveData(getCurrentTime())
    val timeLiveData: LiveData<String>
        get() = _timeLiveData

    private val _isAlarmEnabled = MutableLiveData(AlarmsRepository(getApplication()).isAlarmEnabled)
    val isAlarmEnabled: LiveData<Boolean>
        get() = _isAlarmEnabled

    private val _nextAlarmTriggerTime =
        MutableLiveData(AlarmsRepository(getApplication()).nextAlarm?.let { nextAlarmTime ->
            formatToDigitalClock(
                nextAlarmTime
            )
        })
    val nextAlarmTriggerTime: LiveData<String>
        get() = _nextAlarmTriggerTime

    init {
        initDateAndTimeBroadcastReceiver()
        initAlarmChangedBroadcastReceiver()
    }

    private fun initDateAndTimeBroadcastReceiver() {
        val dateAndTimeChangedBroadcastReceiver = object : DateAndTimeChangedBroadcastReceiver() {
            override fun onTimeTick() {
                _dateLiveData.postValue(getCurrentDate())
                _timeLiveData.postValue(getCurrentTime())
            }
        }

        getApplication<Application>().registerReceiver(
            dateAndTimeChangedBroadcastReceiver,
            DateAndTimeChangedBroadcastReceiver.intentFilter
        )
    }

    private fun initAlarmChangedBroadcastReceiver() {
        val alarmChangedBroadcastReceiver = object : AlarmChangedBroadcastReceiver() {
            override fun onAlarmChanged() {
                _nextAlarmTriggerTime.postValue(AlarmsRepository(getApplication())
                    .nextAlarm?.let { nextAlarmTime ->
                        formatToDigitalClock(
                            nextAlarmTime
                        )

                    })
                _isAlarmEnabled.postValue(AlarmsRepository(getApplication()).isAlarmEnabled)
            }
        }

        getApplication<Application>().registerReceiver(
            alarmChangedBroadcastReceiver,
            AlarmChangedBroadcastReceiver.intentFilter
        )
    }
}
