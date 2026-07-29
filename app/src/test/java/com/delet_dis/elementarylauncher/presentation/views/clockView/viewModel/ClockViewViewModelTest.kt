package com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClockViewViewModelTest {
    @Test
    fun alarmStateRefreshClearsRemovedAlarm() {
        val state = alarmState(null)

        assertFalse(state.isEnabled)
        assertNull(state.displayTime)
    }

    @Test
    fun alarmStateRefreshEnablesPresentAlarm() {
        val state = alarmState(1_800_000L)

        assertTrue(state.isEnabled)
        assertTrue(state.displayTime?.isNotBlank() == true)
    }
}
