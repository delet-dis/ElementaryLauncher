package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.domain.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class TwoByThreeLayoutFragmentViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {
    private val _databaseRecordingsLiveData = MutableLiveData<Array<Card?>>()
    val databaseRecordingsLiveData: LiveData<Array<Card?>>
        get() = _databaseRecordingsLiveData

    init {
        loadDatabaseRecordingsAsCards()
    }

    private fun loadDatabaseRecordingsAsCards() =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getNonEmptyDatabaseRecordingsAsCards()
                .collect { arrayOfCards ->
                    _databaseRecordingsLiveData.postValue(arrayOfCards)
                }
        }
}
