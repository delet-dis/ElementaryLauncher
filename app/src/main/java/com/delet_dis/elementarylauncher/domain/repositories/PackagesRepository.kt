package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackagesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun loadApplicationsPackages(): MutableList<ApplicationInfo> {
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolvedActivities = if (android.os.Build.VERSION.SDK_INT >= 33) {
            context.packageManager.queryIntentActivities(
                launcherIntent,
                PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.queryIntentActivities(launcherIntent, 0)
        }

        return resolvedActivities
            .map { it.activityInfo.applicationInfo }
            .deduplicateByPackage { it.packageName }
            .sortedBy { it.loadLabel(context.packageManager).toString().lowercase() }
            .toMutableList()
    }
}

internal fun <T> Iterable<T>.deduplicateByPackage(packageName: (T) -> String): List<T> =
    distinctBy(packageName)
