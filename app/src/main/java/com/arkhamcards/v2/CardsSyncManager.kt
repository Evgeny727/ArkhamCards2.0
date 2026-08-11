package com.arkhamcards.v2

import com.arkhamcards.v2.domain.exceptions.UnableCreateCardsCacheException
import com.arkhamcards.v2.domain.exceptions.UnableToLoadCardsCacheException
import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

sealed interface CardsSyncState {
    object Idle : CardsSyncState
    data class Loading(val progress: Float) : CardsSyncState
    object UpdateAvailable : CardsSyncState
    object Ready : CardsSyncState
}

sealed interface CardsCacheState {
    object Idle : CardsCacheState
    object Loading : CardsCacheState
    object Ready : CardsCacheState
}

@Singleton
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
        if (_state.value is CardsSyncState.Loading) return
        if (!cardsRepository.isCardsTableExists()) download(language)
        else checkForUpdate(language)
    }

    suspend fun checkForUpdate(language: String) {
        fetchCardsUpdate(language, loadCacheOnError = true) { updateAvailable ->
            if (updateAvailable) _state.value = CardsSyncState.UpdateAvailable
            else loadCache()
        }
    }

    suspend fun updateCardsIfUpdateAvailable(language: String) {
        _state.value = CardsSyncState.Loading(0.0f)
        fetchCardsUpdate(language, forced = true) { updateAvailable ->
            if (updateAvailable) {
                _state.value = CardsSyncState.Loading(0.05f)
                cardsRepository.downloadAllCards(language) { newValue ->
                    _state.value = CardsSyncState.Loading(newValue)
                }
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
        forced: Boolean = false,
        loadCacheOnError: Boolean = false,
        block: suspend (Boolean) -> Unit
    ) {
        cardsRepository.isCardsUpdateAvailable(
            language,
            cardsUpdatedAt.first(),
            forced
        )
            .onSuccess { block(it) }
            .onFailure {
                _errors.tryEmit(it)
                _state.value = CardsSyncState.Ready
                if (loadCacheOnError) loadCache()
            }
    }

    suspend fun download(language: String) {
        _state.value = CardsSyncState.Loading(0.0f)

        cardsRepository.downloadAllCards(language) { newValue ->
            _state.value = CardsSyncState.Loading(newValue)
        }
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

        delay(1.seconds) //slight delay for better visual representation of loading

        if (!cacheReady) _errors.tryEmit(UnableToLoadCardsCacheException())
        _cacheState.value = CardsCacheState.Ready

        _state.value = CardsSyncState.Ready
    }

    suspend fun recreateCache() {
        _cacheState.value = CardsCacheState.Loading

        val cacheRecreated = cardsRepository.recreateCache()

        if (!cacheRecreated) _errors.tryEmit(UnableCreateCardsCacheException())

        _cacheState.value = CardsCacheState.Ready
    }
}