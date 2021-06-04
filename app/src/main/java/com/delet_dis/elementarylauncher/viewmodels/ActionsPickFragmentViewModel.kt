package com.delet_dis.elementarylauncher.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.common.models.Card
import com.delet_dis.elementarylauncher.common.models.LayoutType
import com.delet_dis.elementarylauncher.data.repositories.DatabaseRepository
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ActionsPickFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _databaseRecordingsLiveData = MutableLiveData<List<Card>>()
    val databaseRecordingsLiveData: LiveData<List<Card>>
        get() = _databaseRecordingsLiveData

    private val _isAvailableToEndFirstSetup = MutableLiveData(false)
    val isAvailableToEndFirstSetup: LiveData<Boolean>
        get() = _isAvailableToEndFirstSetup

    init {
        loadDatabaseRecordingsAsCards()
        loadDatabaseRecordingsAsEntitiesParent()
    }

    private fun loadDatabaseRecordingsAsCards() {
        viewModelScope.launch(Dispatchers.IO) {

            if (SharedPreferencesRepository(getApplication()).getLayoutType() == LayoutType.TWO_BY_TWO) {
                DatabaseRepository(getApplication()).deleteAtPosition(5)
                DatabaseRepository(getApplication()).deleteAtPosition(6)
            }

            DatabaseRepository(getApplication()).getAllDatabaseRecordingsAsCards().collect { list ->
                _databaseRecordingsLiveData.postValue(list)
            }
        }
    }

    private fun loadDatabaseRecordingsAsEntitiesParent() {
        viewModelScope.launch {
            DatabaseRepository(getApplication()).getAllDatabaseRecordingsAsEntitiesParentListFlow()
                .collect { list ->
                    if (list.isNotEmpty()) {
                        _isAvailableToEndFirstSetup.postValue(true)
                    } else {
                        _isAvailableToEndFirstSetup.postValue(false)
                    }
                }
        }
    }
}