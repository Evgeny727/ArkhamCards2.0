package com.arkhamcards.v2.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.arkhamcards.v2.UiErrorState
import com.arkhamcards.v2.domain.model.cards.CardsSearchOptions
import com.arkhamcards.v2.domain.model.cards.CardsSearchPreferences
import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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

    private val _searchOptions = MutableStateFlow(CardsSearchOptions())
    val searchOptions = _searchOptions.asStateFlow()

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop = _scrollToTop.asSharedFlow()

    fun updateSearchQuery(query: String) {
        _searchOptions.update { it.copy(searchQuery = query) }
        if (query.isNotBlank()) viewModelScope.launch { _scrollToTop.emit(Unit) }
    }

    fun clearSearchQuery() {
        _searchOptions.update { it.copy(searchQuery = "") }
        viewModelScope.launch { _scrollToTop.emit(Unit) }
    }

    fun onSearchGameTextChange(state: Boolean) {
        _searchOptions.update { it.copy(searchGame = state) }
        if (_searchOptions.value.searchQuery.isNotBlank()) viewModelScope.launch {
            _scrollToTop.emit(Unit)
        }
    }

    fun onSearchFlavorTextChange(state: Boolean) {
        _searchOptions.update { it.copy(searchFlavor = state) }
        if (_searchOptions.value.searchQuery.isNotBlank()) viewModelScope.launch {
            _scrollToTop.emit(Unit)
        }
    }

    fun onSearchBackTextChange(state: Boolean) {
        _searchOptions.update { it.copy(searchBack = state) }
        if (_searchOptions.value.searchQuery.isNotBlank()) viewModelScope.launch {
            _scrollToTop.emit(Unit)
        }
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


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = combine(
        _spoilerState,
        _searchOptions,
        _cardsSearchPreferences
    ) { spoilerState, searchOptions, cardsSearchPreferences ->
        Triple(
            spoilerState,
            searchOptions.copy(searchQuery = searchOptions.searchQuery.trim()),
            cardsSearchPreferences
        )
    }.debounce(200.milliseconds)
        .distinctUntilChanged()
        .flatMapLatest { (spoilerState, searchOptions, searchPreferences) ->
        cardsRepository.searchPaginatedCardsFlow(spoilerState, searchOptions, searchPreferences)
    }.cachedIn(viewModelScope)

}
