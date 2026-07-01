package com.arkhamcards.v2

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiErrorState(val exception: Throwable)

val SUPPORTED_LANGUAGES = setOf("es", "ru", "en", "de", "fr", "pl", "ko", "zh", "zh-cn", "it", "pt", "vi", "uk", "cs",)
val AUDIO_LANGUAGES = setOf("es", "ru", "en", "de", "pl",)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val cardsSyncManager: CardsSyncManager
) : ViewModel() {

    private val _events = MutableSharedFlow<UiErrorState>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emitError(throwable: Throwable) = _events.tryEmit(UiErrorState(throwable))

    val cardsSyncState = cardsSyncManager.state

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

    suspend fun checkIfCardsReady() {
        val language = "en" //coerceLanguage(userUiState.value.language)
        cardsSyncManager.ensureCardsReady(language)
    }

    fun confirmCardsUpdate() {
        viewModelScope.launch {
            val language = "en".dataLanguage //coerceLanguage(userUiState.value.language)
            cardsSyncManager.download(language)
        }
    }

    fun cancelCardsUpdate() = cardsSyncManager.cancelUpdateDialog()

    fun updateCardsIfAvailable() {
        viewModelScope.launch {
            val language = "en" //coerceLanguage(userUiState.value.language)
            cardsSyncManager.updateCardsIfUpdateAvailable(language)
        }
    }

    fun updateLocale(locale: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
        viewModelScope.launch {
            cardsSyncManager.download(coerceLanguage(locale))
        }
    }

    private val String.dataLanguage: String
        get() = if (this in SUPPORTED_LANGUAGES) this else "en"

    private fun coerceLanguage(locale: String): String {
        return if (SUPPORTED_LANGUAGES.contains(locale)) locale else "en"
    }

    fun resolveLanguageTag(language: String): String {
        return if (language.startsWith("zh-Hans") ||
            language.startsWith("zh-CN") ||
            language.startsWith("zh-SG")) "zh"
        else if (language.startsWith("zh-Hant") ||
            language.startsWith("zh-TW") ||
            language.startsWith("zh-HK") ||
            language.startsWith("zh-MO")) "zh-cn"
        else language
    }

}