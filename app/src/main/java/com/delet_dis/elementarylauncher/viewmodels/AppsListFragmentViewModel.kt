package com.delet_dis.elementarylauncher.viewmodels

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.data.repositories.PackagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsListFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _applicationsPackagesLiveData = MutableLiveData<List<ApplicationInfo>>()
    val applicationsPackagesLiveData: LiveData<List<ApplicationInfo>>
        get() = _applicationsPackagesLiveData

    fun loadApplicationsPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _applicationsPackagesLiveData.postValue(
                PackagesRepository(getApplication()).loadApplicationsPackages()
            )
            _isLoading.postValue(false)
        }
    }
}