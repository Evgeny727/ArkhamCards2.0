package com.arkhamcards.v2.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.arkhamcards.v2.UiErrorState
import com.arkhamcards.v2.domain.model.cards.CardsSearchPreferences
import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardsRepository: CardsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _errors = MutableSharedFlow<UiErrorState>(extraBufferCapacity = 1)
    val errors: SharedFlow<UiErrorState> = _errors

    fun emitError(throwable: Throwable) {
        _errors.tryEmit(UiErrorState(throwable))
    }

    private val _spoilerState = MutableStateFlow(false)
    val spoilerState = _spoilerState.asStateFlow()

    fun toggleSpoiler(value: Boolean) {
        _spoilerState.value = value
        viewModelScope.launch { _scrollToTop.emit(Unit) }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop = _scrollToTop.asSharedFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch { _scrollToTop.emit(Unit) }
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
        viewModelScope.launch { _scrollToTop.emit(Unit) }
    }

    private val _cardsSearchPreferences = userPreferencesRepository.cardsSearchPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = CardsSearchPreferences()
    )

    init {
        scrollListOnChange()
    }

    fun scrollListOnChange() {
        viewModelScope.launch {
            _cardsSearchPreferences.collect {
                _scrollToTop.emit(Unit)
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = combine(
        _spoilerState,
        _searchQuery,
        _cardsSearchPreferences
    ) { spoilerState, searchQuery, cardsSearchPreferences ->
        Triple(spoilerState, searchQuery, cardsSearchPreferences)
    }.flatMapLatest { (spoilerState, searchQuery, searchPreferences) ->
        cardsRepository.searchPaginatedCardsFlow(spoilerState, searchQuery, searchPreferences)
    }.cachedIn(viewModelScope)

}
