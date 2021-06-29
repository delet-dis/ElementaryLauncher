package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.viewModel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.data.database.entities.*
import com.delet_dis.elementarylauncher.data.models.ContactActionType
import com.delet_dis.elementarylauncher.data.models.SettingsActionType
import com.delet_dis.elementarylauncher.domain.repositories.DatabaseRepository
import com.delet_dis.elementarylauncher.domain.repositories.PackagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class OnboardingActivityViewModel @Inject constructor(
    application: Application,
    private val databaseRepository: DatabaseRepository
) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isBottomSheetHidden = MutableLiveData(true)
    val isBottomSheetHidden: LiveData<Boolean>
        get() = _isBottomSheetHidden

    private val _applicationsPackagesLiveData = MutableLiveData<List<ApplicationInfo>>()
    val applicationsPackagesLiveData: LiveData<List<ApplicationInfo>>
        get() = _applicationsPackagesLiveData

    private val _settingsActionsLiveData = MutableLiveData<Array<SettingsActionType>>()
    val settingsActionsLiveData: LiveData<Array<SettingsActionType>>
        get() = _settingsActionsLiveData

    fun loadApplicationsPackages() =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _applicationsPackagesLiveData.postValue(
                PackagesRepository(getApplication()).loadApplicationsPackages()
            )
            _isLoading.postValue(false)
        }

    fun insertApp(packageName: String, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertWithOverride(
                App(packageName, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }

    fun insertWidget(widgetId: Int, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertWithOverride(
                Widget(widgetId, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }

    fun loadSettingsActionsPackages() =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _settingsActionsLiveData.postValue(
                SettingsActionType.values()
            )
            _isLoading.postValue(false)
        }

    fun insertSettingsAction(action: String, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertWithOverride(
                SettingsAction(action, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }

    fun insertContact(actionType: ContactActionType, uri: String, position: Int) =
        when (actionType) {
            ContactActionType.CARD -> {
                viewModelScope.launch(Dispatchers.IO) {
                    databaseRepository.insertWithOverride(
                        Contact(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
            ContactActionType.CALL -> {
                viewModelScope.launch(Dispatchers.IO) {
                    databaseRepository.insertWithOverride(
                        ContactCall(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
            ContactActionType.SMS -> {
                viewModelScope.launch(Dispatchers.IO) {
                    databaseRepository.insertWithOverride(
                        ContactSMS(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
        }

    fun deleteAtPosition(position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.deleteAtPosition(position)

            _isBottomSheetHidden.postValue(true)
        }

    fun checkForPermission(
        permissionToCheck: String,
        onSuccessFunction: () -> Unit,
        onFailureFunction: () -> Unit
    ) =
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                permissionToCheck
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onSuccessFunction()
        } else {
            onFailureFunction()
        }
}
