package com.delet_dis.elementarylauncher.ui.activities.launcherActivity.fragments.twoByThreeLayoutFragment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.common.models.Card
import com.delet_dis.elementarylauncher.data.repositories.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TwoByThreeLayoutFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _databaseRecordingsLiveData = MutableLiveData<Array<Card?>>()
    val databaseRecordingsLiveData: LiveData<Array<Card?>>
        get() = _databaseRecordingsLiveData

    init {
        loadDatabaseRecordingsAsCards()
    }

    private fun loadDatabaseRecordingsAsCards() {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseRepository(getApplication()).getNonEmptyDatabaseRecordingsAsCards()
                .collect { arrayOfCards ->
                    _databaseRecordingsLiveData.postValue(arrayOfCards)
                }
        }
    }
}