package com.arkhamcards.v2

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiErrorState(val exception: Throwable)

val SUPPORTED_LANGUAGES = setOf("es", "ru", "en", "de", "fr", "pl", "ko", "zh", "zh-cn", "it", "pt", "vi", "uk", "cs")
val AUDIO_LANGUAGES = setOf("es", "ru", "en", "de", "pl")

@HiltViewModel
class AppViewModel @Inject constructor(
    private val cardsSyncManager: CardsSyncManager,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<UiErrorState>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emitError(throwable: Throwable) = _events.tryEmit(UiErrorState(throwable))

    val cardsSyncState = cardsSyncManager.state
    val cardsCacheState = cardsSyncManager.cacheState

    val scaleFactorState = userPreferencesRepository.scaleFactor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1f
    )

    val themeState = userPreferencesRepository.isDarkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    init {
        observeCardsErrors()
    }

    private fun observeCardsErrors() {
        viewModelScope.launch {
            cardsSyncManager.errors.collect {
                emitError(it)
            }
        }
    }

    suspend fun checkIfCardsReady(language: String) {
        cardsSyncManager.ensureCardsReady(language)
    }

    fun confirmCardsUpdate(language: String) {
        viewModelScope.launch {
            cardsSyncManager.download(language)
        }
    }

    fun cancelCardsUpdate() {
        if (cardsCacheState != CardsCacheState.Loading) {
            viewModelScope.launch {
                cardsSyncManager.loadCache()
            }
        }
    }

    fun updateCardsIfAvailable(language: String) {
        if (cardsSyncState != CardsSyncState.Loading) {
            viewModelScope.launch {
                cardsSyncManager.updateCardsIfUpdateAvailable(language)
            }
        }
    }

    fun updateLocale(locale: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
        viewModelScope.launch {
            cardsSyncManager.download(locale)
        }
    }

    fun resolveLanguageTag(language: String): String {
        return if (language.startsWith("zh-Hans") ||
            language.startsWith("zh-CN") ||
            language.startsWith("zh-SG")) "zh"
        else if (language.startsWith("zh-Hant") ||
            language.startsWith("zh-TW") ||
            language.startsWith("zh-HK") ||
            language.startsWith("zh-MO")) "zh-cn"
        else language.substringBefore("-")
    }

}