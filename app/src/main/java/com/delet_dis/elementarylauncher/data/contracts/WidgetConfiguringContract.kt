package com.delet_dis.elementarylauncher.data.contracts

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class WidgetConfiguringContract : ActivityResultContract<Int, Pair<Boolean, Int?>>() {

    override fun createIntent(context: Context, input: Int?): Intent {

        val appWidgetInfo: AppWidgetProviderInfo? =
            input?.let { AppWidgetManager.getInstance(context).getAppWidgetInfo(it) }

        return Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
            if (appWidgetInfo != null) {
                component = appWidgetInfo.configure
            }

            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Int?> {
        intent?.let {
            if (resultCode == Activity.RESULT_OK) {
                return Pair(
                    true, it.extras?.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, -1
                    )
                )
            } else if (resultCode == Activity.RESULT_CANCELED) {
                return Pair(
                    false, it.extras?.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, -1
                    )
                )
            }
        }
        return Pair(false, null)
    }
}