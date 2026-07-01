package com.arkhamcards.v2.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

@Immutable
data class Language(
    val languageTag: String,
    val listSeparator: String,
    val colon: String,
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

internal fun language(lang: String) = language.copy(
    languageTag = lang,
    listSeparator = getLocalizedListSeparator(lang),
    colon = getLocalizedColon(lang),
)

val language = Language(
    languageTag = "en",
    listSeparator = ", ",
    colon = ": ",
)

val LocalLanguage = compositionLocalOf { language }