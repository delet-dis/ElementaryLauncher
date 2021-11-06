package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.appsListFragment.viewModel

import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.domain.repositories.PackagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsListFragmentViewModel @Inject constructor(private val packagesRepository: PackagesRepository) :
    ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _applicationsPackagesLiveData = MutableLiveData<List<ApplicationInfo>>()
    val applicationsPackagesLiveData: LiveData<List<ApplicationInfo>>
        get() = _applicationsPackagesLiveData

    fun loadApplicationsPackages() =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _applicationsPackagesLiveData.postValue(
                packagesRepository.loadApplicationsPackages()
            )
            _isLoading.postValue(false)
        }
}
