package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.screenLayoutPickFragment.viewModel

import androidx.lifecycle.ViewModel
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScreenLayoutPickFragmentViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    fun setLayoutType(layoutType: LayoutType) =
        sharedPreferencesRepository.setLayoutType(layoutType)

    fun setTempLayoutType(layoutNumber: Int) =
        sharedPreferencesRepository.setTempLayoutType(layoutNumber)

    fun getTempLayoutType() =
        sharedPreferencesRepository.getTempLayoutType()
}