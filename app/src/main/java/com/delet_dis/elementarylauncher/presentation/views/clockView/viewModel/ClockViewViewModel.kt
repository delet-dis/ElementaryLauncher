package com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delet_dis.elementarylauncher.data.broadcastReceivers.AlarmChangedBroadcastReceiver
import com.delet_dis.elementarylauncher.data.broadcastReceivers.DateAndTimeChangedBroadcastReceiver
import com.delet_dis.elementarylauncher.domain.helpers.formatToDigitalClock
import com.delet_dis.elementarylauncher.domain.helpers.getCurrentDate
import com.delet_dis.elementarylauncher.domain.helpers.getCurrentTime
import com.delet_dis.elementarylauncher.domain.repositories.AlarmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ClockViewViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val alarmsRepository: AlarmsRepository
) : ViewModel() {

    private val _dateLiveData = MutableLiveData(getCurrentDate())
    val dateLiveData: LiveData<String> = _dateLiveData

    private val _timeLiveData = MutableLiveData(getCurrentTime())
    val timeLiveData: LiveData<String> = _timeLiveData

    private val _isAlarmEnabled = MutableLiveData<Boolean>()
    val isAlarmEnabled: LiveData<Boolean> = _isAlarmEnabled

    private val _nextAlarmTriggerTime = MutableLiveData<String?>()
    val nextAlarmTriggerTime: LiveData<String?> = _nextAlarmTriggerTime

    private val dateAndTimeChangedReceiver = object : DateAndTimeChangedBroadcastReceiver() {
        override fun onTimeTick() {
            _dateLiveData.postValue(getCurrentDate())
            _timeLiveData.postValue(getCurrentTime())
        }
    }

    private val alarmChangedReceiver = object : AlarmChangedBroadcastReceiver() {
        override fun onAlarmChanged() = refreshAlarmState()
    }

    init {
        refreshAlarmState()
        ContextCompat.registerReceiver(
            context,
            dateAndTimeChangedReceiver,
            DateAndTimeChangedBroadcastReceiver.intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        ContextCompat.registerReceiver(
            context,
            alarmChangedReceiver,
            AlarmChangedBroadcastReceiver.intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    internal fun refreshAlarmState() {
        val state = alarmState(alarmsRepository.nextAlarm)
        _nextAlarmTriggerTime.value = state.displayTime
        _isAlarmEnabled.value = state.isEnabled
    }

    override fun onCleared() {
        runCatching { context.unregisterReceiver(dateAndTimeChangedReceiver) }
        runCatching { context.unregisterReceiver(alarmChangedReceiver) }
    }
}

internal data class AlarmState(val isEnabled: Boolean, val displayTime: String?)

internal fun alarmState(nextAlarm: Long?): AlarmState =
    AlarmState(
        isEnabled = nextAlarm != null,
        displayTime = nextAlarm?.let(::formatToDigitalClock)
    )
