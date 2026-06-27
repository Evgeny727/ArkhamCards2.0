package com.arkhamcards.v2

import android.util.Log
import com.arkhamcards.v2.domain.repository.CardsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface CardsSyncState {
    object Idle : CardsSyncState
    object Loading : CardsSyncState
    object UpdateAvailable : CardsSyncState
    object Ready : CardsSyncState
}

class CardsSyncManager @Inject constructor(private val cardsRepository: CardsRepository) {

    private val _state = MutableStateFlow<CardsSyncState>(CardsSyncState.Idle)
    val state: StateFlow<CardsSyncState> = _state.asStateFlow()

    private val _errors = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val errors: SharedFlow<Throwable> = _errors

    suspend fun ensureCardsReady(language: String) {
        if (!cardsRepository.isCardsTableExists()) download(language)
        //else checkForUpdate(language)
        //download(language)
    }

    suspend fun checkForUpdate(language: String) {
        fetchCardsUpdate(language) { updateAvailable ->
            if (updateAvailable) _state.value = CardsSyncState.UpdateAvailable
            else {
                _state.value = if (cardsRepository.loadCache()) CardsSyncState.Ready
                else CardsSyncState.UpdateAvailable
            }
        }
    }

    suspend fun updateCardsIfUpdateAvailable(language: String) {
        _state.value = CardsSyncState.Loading
        fetchCardsUpdate(language) { updateAvailable ->
            if (updateAvailable) {
                cardsRepository.downloadAllCards(language)
                    .onSuccess { _state.value = CardsSyncState.Ready }
                    .onFailure {
                        _errors.tryEmit(it)
                        _state.value = CardsSyncState.Ready
                    }
            } else {
                _state.value = CardsSyncState.Ready
            }
        }
    }

    private suspend fun fetchCardsUpdate(
        language: String,
        block: suspend (Boolean) -> Unit
    ): Result<Boolean> {
        return cardsRepository.isCardsUpdateAvailable(
            language,
            null
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
            .onSuccess { _state.value = CardsSyncState.Ready }
            .onFailure {
                Log.e("Error", it.message.toString())
                _errors.tryEmit(it)
                _state.value = CardsSyncState.Ready
            }
    }

    fun cancelUpdateDialog() {
        _state.value = if (cardsRepository.loadCache()) CardsSyncState.Ready
        else CardsSyncState.UpdateAvailable
    }
}