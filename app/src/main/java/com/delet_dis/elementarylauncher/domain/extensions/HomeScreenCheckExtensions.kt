package com.delet_dis.elementarylauncher.domain.extensions

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_HOME

fun checkIfAppIsDefaultLauncher(context: Context): Boolean {
    val intent = Intent(ACTION_MAIN)
    intent.addCategory(CATEGORY_HOME)

    val packageManager = context.packageManager

    val info = packageManager.resolveActivity(intent, 0)
    val homescreenApplicationLabel =
        packageManager.getApplicationLabel(info!!.activityInfo.applicationInfo)

    return homescreenApplicationLabel == context.applicationInfo.loadLabel(packageManager)
}
