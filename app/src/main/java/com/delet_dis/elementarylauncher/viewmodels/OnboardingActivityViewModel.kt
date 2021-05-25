package com.delet_dis.elementarylauncher.viewmodels

import android.app.Application
import android.appwidget.AppWidgetProviderInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.common.models.ContactActionType
import com.delet_dis.elementarylauncher.common.models.SettingsActionType
import com.delet_dis.elementarylauncher.data.database.entities.*
import com.delet_dis.elementarylauncher.data.repositories.DatabaseRepository
import com.delet_dis.elementarylauncher.data.repositories.PackagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class OnboardingActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isBottomSheetHidden = MutableLiveData(true)
    val isBottomSheetHidden: LiveData<Boolean>
        get() = _isBottomSheetHidden

    private val _applicationsPackagesLiveData = MutableLiveData<List<ApplicationInfo>>()
    val applicationsPackagesLiveData: LiveData<List<ApplicationInfo>>
        get() = _applicationsPackagesLiveData

    private val _shortcutsPackagesLiveData = MutableLiveData<MutableList<ResolveInfo>>()
    val shortcutsPackagesLiveData: LiveData<MutableList<ResolveInfo>>
        get() = _shortcutsPackagesLiveData

    private val _widgetsPackagesLiveData = MutableLiveData<MutableList<AppWidgetProviderInfo>?>()
    val widgetsPackagesLiveData: LiveData<MutableList<AppWidgetProviderInfo>?>
        get() = _widgetsPackagesLiveData

    private val _settingsActionsLiveData = MutableLiveData<Array<SettingsActionType>>()
    val settingsActionsLiveData: LiveData<Array<SettingsActionType>>
        get() = _settingsActionsLiveData

    private val _pickedWidgetIdLiveData = MutableLiveData<Int>(null)
    val pickedWidgetIdLiveData: LiveData<Int>
        get() = _pickedWidgetIdLiveData

    fun loadApplicationsPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _applicationsPackagesLiveData.postValue(
                PackagesRepository(getApplication()).loadApplicationsPackages()
            )
            _isLoading.postValue(false)
        }
    }

    fun insertApp(packageName: String, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).insertWithOverride(
                App(packageName, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }
    }

    fun loadShortcutsPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _shortcutsPackagesLiveData.postValue(
                PackagesRepository(getApplication()).loadShortcutsPackages()
            )
            _isLoading.postValue(false)
        }
    }

    fun insertShortcut(activityShortcutName: String, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).insertWithOverride(
                Shortcut(activityShortcutName, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }
    }

    fun loadWidgetsPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _widgetsPackagesLiveData.postValue(
                PackagesRepository(getApplication()).loadWidgetsPackages()
            )
            _isLoading.postValue(false)
        }
    }

    fun insertWidget(widgetId: Int, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).insertWithOverride(
                Widget(widgetId, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }
    }

    fun loadSettingsActionsPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _settingsActionsLiveData.postValue(
                SettingsActionType.values()
            )
            _isLoading.postValue(false)
        }
    }

    fun insertSettingsAction(action: String, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).insertWithOverride(
                SettingsAction(action, position),
                position
            )

            _isBottomSheetHidden.postValue(true)
        }
    }

    fun insertContact(actionType: ContactActionType, uri: String, position: Int) {
        when (actionType) {
            ContactActionType.CARD -> {
                viewModelScope.launch(Dispatchers.IO) {
                    DatabaseRepository(getApplication()).insertWithOverride(
                        Contact(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
            ContactActionType.CALL -> {
                viewModelScope.launch(Dispatchers.IO) {
                    DatabaseRepository(getApplication()).insertWithOverride(
                        ContactCall(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
            ContactActionType.SMS -> {
                viewModelScope.launch(Dispatchers.IO) {
                    DatabaseRepository(getApplication()).insertWithOverride(
                        ContactSMS(uri, position),
                        position
                    )

                    _isBottomSheetHidden.postValue(true)
                }
            }
        }
    }

    fun deleteAtPosition(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).deleteAtPosition(position)

            _isBottomSheetHidden.postValue(true)
        }
    }

    fun checkForPermission(
        permissionToCheck: String,
        onSuccessFunction: () -> Unit,
        onFailureFunction: () -> Unit
    ) {
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
}