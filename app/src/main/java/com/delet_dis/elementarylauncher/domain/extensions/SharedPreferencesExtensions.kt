package com.delet_dis.elementarylauncher.domain.extensions

import android.content.Context
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository

fun isOnboardingPassed(context: Context): Boolean {
    return SharedPreferencesRepository(context).isOnboardingPassed()
}