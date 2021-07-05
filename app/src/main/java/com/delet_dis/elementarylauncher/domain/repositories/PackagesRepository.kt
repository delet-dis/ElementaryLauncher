package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager


class PackagesRepository(private val context: Context) {

    fun loadApplicationsPackages(): MutableList<ApplicationInfo> {

        val launchableInstalledApps: ArrayList<ApplicationInfo> = ArrayList()

        with(context) {
            packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA).forEach { applicationInfo ->
                    if (
                        context.packageManager
                            .getLaunchIntentForPackage(applicationInfo.packageName) != null
                    ) {
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
}
