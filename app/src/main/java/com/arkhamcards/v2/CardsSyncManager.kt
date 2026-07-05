package com.arkhamcards.v2

import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed interface CardsSyncState {
    object Idle : CardsSyncState
    object Loading : CardsSyncState
    object UpdateAvailable : CardsSyncState
    object Ready : CardsSyncState
}

sealed interface CardsCacheState {
    object Idle : CardsCacheState
    object Loading : CardsCacheState
    object Ready : CardsCacheState
    object Error : CardsCacheState
}

class CardsSyncManager @Inject constructor(
    private val cardsRepository: CardsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    private val _state = MutableStateFlow<CardsSyncState>(CardsSyncState.Idle)
    val state: StateFlow<CardsSyncState> = _state.asStateFlow()

    private val _cacheState = MutableStateFlow<CardsCacheState>(CardsCacheState.Idle)
    val cacheState: StateFlow<CardsCacheState> = _cacheState.asStateFlow()

    private val _errors = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val errors: SharedFlow<Throwable> = _errors

    private val cardsUpdatedAt = userPreferencesRepository.cardsUpdatedAt

    suspend fun ensureCardsReady(language: String) {
        if (!cardsRepository.isCardsTableExists()) download(language)
        else checkForUpdate(language)
    }

    suspend fun checkForUpdate(language: String) {
        fetchCardsUpdate(language) { updateAvailable ->
            if (updateAvailable) _state.value = CardsSyncState.UpdateAvailable
            else loadCache()
        }
    }

    suspend fun updateCardsIfUpdateAvailable(language: String) {
        _state.value = CardsSyncState.Loading
        fetchCardsUpdate(language) { updateAvailable ->
            if (updateAvailable) {
                cardsRepository.downloadAllCards(language)
                    .onSuccess {
                        userPreferencesRepository.saveCardsUpdatedTimestamp(it)
                        _state.value = CardsSyncState.Ready
                    }
                    .onFailure {
                        _errors.tryEmit(it)
                        _state.value = CardsSyncState.Ready
                    }
            } else {
                _state.value = CardsSyncState.Ready
            }
        }
    }

    private suspend inline fun fetchCardsUpdate(
        language: String,
        block: suspend (Boolean) -> Unit
    ) {
        cardsRepository.isCardsUpdateAvailable(
            language,
            cardsUpdatedAt.first()
        )
            .onSuccess { block(it) }
            .onFailure {
                _errors.tryEmit(it)
                _state.value = CardsSyncState.Ready
            }
    }

    suspend fun download(language: String) {
        _state.value = CardsSyncState.Loading

        cardsRepository.downloadAllCards(language)
            .onSuccess {
                userPreferencesRepository.saveCardsUpdatedTimestamp(it)
                _state.value = CardsSyncState.Ready
            }
            .onFailure {
                _errors.tryEmit(it)
                _state.value = CardsSyncState.Ready
            }
    }

    suspend fun loadCache() {
        _cacheState.value = CardsCacheState.Loading
        val cacheReady = cardsRepository.loadCache()

        if (cacheReady) _cacheState.value = CardsCacheState.Ready
        else _cacheState.value = CardsCacheState.Error

        _state.value = CardsSyncState.Ready
    }
}