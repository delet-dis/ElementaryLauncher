package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.data.models.SizeType
import com.delet_dis.elementarylauncher.domain.extensions.findLayoutType
import com.delet_dis.elementarylauncher.domain.extensions.findScale

class SharedPreferencesRepository(private val context: Context) {

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(
            SharedPreferencesConstantsRepository.appSettings,
            Context.MODE_PRIVATE
        )
    }

    fun setScale(size: SizeType) {
        getSharedPreferences().edit()
            .putDouble(
                SharedPreferencesConstantsRepository.scaleCoefficient,
                size.scaleCoefficient.toDouble()
            )
            .apply()
    }

    fun getSizeType(): SizeType {
        return findScale(
            getSharedPreferences().getDouble(
                SharedPreferencesConstantsRepository.scaleCoefficient,
                SizeType.MEDIUM.scaleCoefficient.toDouble()
            ).toFloat()
        )
    }

    fun setLayoutType(layoutType: LayoutType) {
        setNumberOfRows(layoutType.numberOfRows)
    }

    fun getLayoutType(): LayoutType {
        return findLayoutType(getNumberOfRows())
    }

    fun setOnboardingPassed() {
        getSharedPreferences().edit()
            .putBoolean(SharedPreferencesConstantsRepository.onboardingStatus, true).apply()
    }

    fun setActionsPicked() {
        getSharedPreferences().edit()
            .putBoolean(SharedPreferencesConstantsRepository.actionsPicked, true).apply()
    }

    fun isActionsPicked(): Boolean {
        return getSharedPreferences().getBoolean(
            SharedPreferencesConstantsRepository.actionsPicked,
            false
        )
    }

    fun isOnboardingPassed(): Boolean {
        return getSharedPreferences().getBoolean(
            SharedPreferencesConstantsRepository.onboardingStatus,
            false
        )
    }

    fun isSettingsShowcaseShown(): Boolean {
        return getSharedPreferences().getBoolean(
            SharedPreferencesConstantsRepository.settingsShowcaseStatus,
            false
        )
    }

    fun setSettingsShowcaseShown() {
        getSharedPreferences().edit()
            .putBoolean(SharedPreferencesConstantsRepository.settingsShowcaseStatus, true).apply()
    }

    private fun setNumberOfRows(girdId: Int) {
        getSharedPreferences().edit()
            .putInt(SharedPreferencesConstantsRepository.numberOfRows, girdId).apply()
    }

    private fun getNumberOfRows(): Int {
        return getSharedPreferences().getInt(SharedPreferencesConstantsRepository.numberOfRows, 0)
    }

    private fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    private fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(
            getLong(
                key,
                java.lang.Double.doubleToRawLongBits(default)
            )
        )

    private object SharedPreferencesConstantsRepository {
        const val settingsShowcaseStatus = "SETTINGS_SHOWCASE_STATUS"
        const val appSettings = "APP_SETTINGS"
        const val scaleCoefficient = "SCALE_COEFFICIENT"
        const val numberOfRows = "NUMBER_OF_ROWS"
        const val onboardingStatus = "ONBOARDING_STATUS"
        const val actionsPicked = "ACTIONS_PICKED"
    }
}