package com.arkhamcards.v2.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object CustomTheme {
    val typography: CustomTypography
        @ReadOnlyComposable
        @Composable
        get() = LocalCustomTypography.current
    val colors: CustomColors
        @ReadOnlyComposable
        @Composable
        get() = LocalCustomColors.current
    val shapes: CustomShape
        @ReadOnlyComposable
        @Composable
        get() = LocalCustomShapes.current
    val language: Language
        @ReadOnlyComposable
        @Composable
        get() = LocalLanguage.current
}

@Composable
fun ArkhamCardsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lang: String,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val typography = typography(colorScheme, lang, usePingFang = false)
    val language = language(lang)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.l30.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    CompositionLocalProvider(
        LocalCustomColors provides colorScheme,
        LocalCustomTypography provides typography,
        LocalCustomShapes provides CustomTheme.shapes,
        LocalLanguage provides language,
        content = content
    )
}