package com.delet_dis.elementarylauncher.common.extensions

import android.content.Context
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository

fun isOnboardingPassed(context: Context): Boolean {
    return SharedPreferencesRepository(context).isOnboardingPassed()
}