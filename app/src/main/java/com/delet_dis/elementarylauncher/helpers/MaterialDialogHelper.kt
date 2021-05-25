package com.delet_dis.elementarylauncher.helpers

import android.app.Activity
import android.content.DialogInterface
import com.delet_dis.elementarylauncher.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun buildPermissionAlertDialog(
    activity: Activity,
    messageResId: Int,
    positiveButtonFunction: () -> Unit
) {
    with(activity) {
        MaterialAlertDialogBuilder(
            this,
            R.style.MaterialDialogOnboardingStyle
        )
            .setTitle(
                applicationContext
                    .getString(R.string.permissionRequiredTitle)
            )
            .setMessage(
                applicationContext
                    .getString(messageResId)
            )
            .setPositiveButton(
                applicationContext
                    .getText(R.string.permissionRequiredPositiveButton)
            ) { _: DialogInterface, _: Int ->
                positiveButtonFunction.invoke()
            }
            .setNegativeButton(
                applicationContext
                    .getText(R.string.permissionRequiredNegativeButton)
            )
            { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .show()
    }
}
