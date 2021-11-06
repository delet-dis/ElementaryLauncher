package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.interfaceScalePickFragment.viewModel

import androidx.lifecycle.ViewModel
import com.delet_dis.elementarylauncher.data.models.SizeType
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterfaceScalePickFragmentViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    fun setScale(sizeType: SizeType) =
        sharedPreferencesRepository.setScale(sizeType)
}