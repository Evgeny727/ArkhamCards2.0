package com.arkhamcards.v2.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhamcards.v2.UiErrorState
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _errors = MutableSharedFlow<UiErrorState>(extraBufferCapacity = 1)
    val errors: SharedFlow<UiErrorState> = _errors

    fun emitError(throwable: Throwable) {
        _errors.tryEmit(UiErrorState(throwable))
    }

    val isIncludeEnglishSearchResultsState: StateFlow<Boolean> =
        userPreferencesRepository.isIncludeEnglishSearchResults.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val collectionState: StateFlow<Collection> = userPreferencesRepository.collection.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Collection(persistentListOf(), persistentListOf())
    )

    val ignoreCollectionState: StateFlow<Boolean> = userPreferencesRepository.ignoreCollection.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val tabooSetIdState: StateFlow<Int> = userPreferencesRepository.tabooSetId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

    fun selectTheme(theme: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemePreference(theme)
        }
    }

    fun setScaleFactor(scaleFactor: Float) {
        viewModelScope.launch {
            userPreferencesRepository.saveScaleFactorPreference(scaleFactor)
        }
    }

    fun setTaboo(tabooSetId: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveTabooSetPreference(tabooSetId)
        }
    }

    fun setCollection(collection: Collection) {
        viewModelScope.launch {
            userPreferencesRepository.saveCollectionPreference(collection)
        }
    }

    fun setIgnoreCollection(ignoreCollection: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIgnoreCollectionPreference(ignoreCollection)
        }
    }

    fun setEnglishSearchResults(isInclude: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIncludeEnglishSearchResults(isInclude)
        }
    }
}