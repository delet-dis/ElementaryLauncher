package com.delet_dis.elementarylauncher.domain.contracts

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contract class used as required step during widget configuring.
 */
class WidgetPickingContract : ActivityResultContract<Int, Pair<Boolean, Int?>>() {

    override fun createIntent(context: Context, input: Int): Intent =
        Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
            putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                input
            )
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Int?> {
        intent?.let { notNullIntent ->
            if (resultCode == Activity.RESULT_OK) {
                return Pair(
                    true, notNullIntent.extras?.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, -1
                    )
                )
            } else if (resultCode == Activity.RESULT_CANCELED) {
                return Pair(
                    false, notNullIntent.extras?.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, -1
                    )
                )
            }
        }
        return Pair(false, null)
    }
}
