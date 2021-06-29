package com.delet_dis.elementarylauncher.domain.repositories

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo


class PackagesRepository(private val context: Context) {

    fun loadApplicationsPackages(): MutableList<ApplicationInfo> {

        val launchableInstalledApps: ArrayList<ApplicationInfo> = ArrayList()

        with(context) {
            packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA).forEach { applicationInfo ->
                    if (context.packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                        launchableInstalledApps.add(applicationInfo)

                    }
                }

            launchableInstalledApps.sortWith { o1, o2 ->
                o1.loadLabel(packageManager).toString()
                    .compareTo(o2.loadLabel(packageManager).toString())
            }

            return launchableInstalledApps
        }
    }


    fun loadShortcutsPackages(): MutableList<ResolveInfo> {
        with(context) {
            val shortcutIntent = Intent(Intent.ACTION_CREATE_SHORTCUT)
            val shortcutsList = packageManager.queryIntentActivities(shortcutIntent, 0)

            shortcutsList.sortWith { o1, o2 ->
                o1.loadLabel(packageManager).toString()
                    .compareTo(o2.loadLabel(packageManager).toString())
            }

            return shortcutsList
        }
    }

    fun loadWidgetsPackages(): MutableList<AppWidgetProviderInfo>? {
        with(context) {
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val widgetsList = appWidgetManager.installedProviders

            widgetsList.sortWith { o1, o2 ->
                o1.loadLabel(packageManager).toString()
                    .compareTo(o2.loadLabel(packageManager).toString())
            }

            return widgetsList
        }
    }
}
