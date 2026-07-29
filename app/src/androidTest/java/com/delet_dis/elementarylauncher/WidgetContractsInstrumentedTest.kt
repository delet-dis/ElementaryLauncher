package com.delet_dis.elementarylauncher

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.delet_dis.elementarylauncher.domain.contracts.WidgetConfiguringContract
import com.delet_dis.elementarylauncher.domain.contracts.WidgetPickingContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WidgetContractsInstrumentedTest {
    @Test
    fun widgetCancellationReturnsAllocatedIdForCleanup() {
        val widgetId = 42
        val result = WidgetPickingContract().parseResult(
            Activity.RESULT_CANCELED,
            Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        )

        assertFalse(result.first)
        assertEquals(widgetId, result.second)
    }

    @Test
    fun configurationCancellationReturnsWidgetId() {
        val widgetId = 43
        val result = WidgetConfiguringContract().parseResult(
            Activity.RESULT_CANCELED,
            Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        )

        assertFalse(result.first)
        assertEquals(widgetId, result.second)
    }
}
