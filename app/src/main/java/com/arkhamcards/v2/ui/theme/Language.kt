package com.arkhamcards.v2.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class Language(
    val languageTag: String,
    val listSeparator: String,
    val colon: String,
    val arkhamDbDomain: String,
    val usePingFang: Boolean = false, //iOS only
    //val audioLangs: List<String> = emptyList(),
)

private fun getLocalizedListSeparator(lang: String): String = when (lang) {
    "zh", "zh-CN" -> "、"
    else -> ", "
}

private fun getLocalizedColon(lang: String): String = when (lang) {
    "fr" -> " : "
    else -> ": "
}

private val NON_LOCALIZED_CARDS = setOf("en", "cs", "vi");

private fun getLocalizedArkhamDbDomain(lang: String): String = when (lang) {
    in NON_LOCALIZED_CARDS -> "https://arkhamdb.com"
    else -> "https://${lang}.arkhamdb.com"
}

internal fun language(lang: String) = language.copy(
    languageTag = lang,
    listSeparator = getLocalizedListSeparator(lang),
    colon = getLocalizedColon(lang),
    arkhamDbDomain = getLocalizedArkhamDbDomain(lang),
)

val language = Language(
    languageTag = "en",
    listSeparator = ", ",
    colon = ": ",
    arkhamDbDomain = "https://arkhamdb.com",
)

val LocalLanguage = staticCompositionLocalOf { language }