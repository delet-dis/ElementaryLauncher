package com.delet_dis.elementarylauncher.data.contracts

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class WidgetPickingContract : ActivityResultContract<Int, Pair<Boolean, Int?>>() {

    override fun createIntent(context: Context, input: Int?): Intent {
        return Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
            putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                input
            )
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