package com.delet_dis.elementarylauncher.domain.widgets

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context

/**
 * Owns the launcher's single stable widget host. Activities control listening with lifecycle;
 * shortcut views only request host views from this owner.
 */
class LauncherWidgetHost private constructor(context: Context) {
    private val appContext = context.applicationContext
    private val host = AppWidgetHost(appContext, HOST_ID)
    private val manager = AppWidgetManager.getInstance(appContext)

    fun startListening() {
        runCatching { host.startListening() }
    }

    fun stopListening() {
        runCatching { host.stopListening() }
    }

    fun allocateId(): Int = host.allocateAppWidgetId()

    fun deleteId(widgetId: Int) {
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            host.deleteAppWidgetId(widgetId)
        }
    }

    fun providerInfo(widgetId: Int): AppWidgetProviderInfo? = manager.getAppWidgetInfo(widgetId)

    fun createView(context: Context, widgetId: Int): AppWidgetHostView? {
        val provider = providerInfo(widgetId) ?: return null
        return host.createView(context, widgetId, provider).apply {
            setAppWidget(widgetId, provider)
        }
    }

    companion object {
        const val HOST_ID = 100

        @Volatile
        private var instance: LauncherWidgetHost? = null

        fun get(context: Context): LauncherWidgetHost =
            instance ?: synchronized(this) {
                instance ?: LauncherWidgetHost(context).also { instance = it }
            }
    }
}
