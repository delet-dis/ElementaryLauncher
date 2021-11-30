package com.delet_dis.elementarylauncher.domain.extensions

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository

fun isOnboardingPassed(context: Context): Boolean =
    SharedPreferencesRepository(context).isOnboardingPassed()

fun SharedPreferences.Editor.putDouble(key: String, double: Double): SharedPreferences.Editor =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

fun SharedPreferences.getDouble(key: String, default: Double) =
    java.lang.Double.longBitsToDouble(
        getLong(
            key,
            java.lang.Double.doubleToRawLongBits(default)
        )
    )