package com.delet_dis.elementarylauncher.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.data.models.SizeType
import com.delet_dis.elementarylauncher.domain.extensions.findLayoutType
import com.delet_dis.elementarylauncher.domain.extensions.findScale
import com.delet_dis.elementarylauncher.domain.extensions.getDouble
import com.delet_dis.elementarylauncher.domain.extensions.putDouble

class SharedPreferencesRepository(private val context: Context) {

    private fun getSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(
            appSettings,
            Context.MODE_PRIVATE
        )

    fun setScale(size: SizeType) =
        getSharedPreferences().edit()
            .putDouble(
                scaleCoefficient,
                size.scaleCoefficient.toDouble()
            )
            .apply()

    fun getSizeType(): SizeType =
        findScale(
            getSharedPreferences().getDouble(
                scaleCoefficient,
                SizeType.MEDIUM.scaleCoefficient.toDouble()
            ).toFloat()
        )

    fun setLayoutType(layoutType: LayoutType) =
        setNumberOfRows(layoutType.numberOfRows)

    fun getLayoutType(): LayoutType =
        findLayoutType(getNumberOfRows())

    fun setOnboardingPassed() =
        getSharedPreferences().edit()
            .putBoolean(onboardingStatus, true).apply()

    fun setActionsPicked() =
        getSharedPreferences().edit()
            .putBoolean(actionsPicked, true).apply()

    fun isActionsPicked(): Boolean =
        getSharedPreferences().getBoolean(
            actionsPicked,
            false
        )

    fun isOnboardingPassed(): Boolean =
        getSharedPreferences().getBoolean(
            onboardingStatus,
            false
        )

    private fun setNumberOfRows(girdId: Int) =
        getSharedPreferences().edit()
            .putInt(numberOfRows, girdId).apply()

    private fun getNumberOfRows(): Int =
        getSharedPreferences().getInt(numberOfRows, 0)

    companion object SharedPreferencesConstantsRepository {
        const val appSettings = "APP_SETTINGS"
        const val scaleCoefficient = "SCALE_COEFFICIENT"
        const val numberOfRows = "NUMBER_OF_ROWS"
        const val onboardingStatus = "ONBOARDING_STATUS"
        const val actionsPicked = "ACTIONS_PICKED"
    }
}
