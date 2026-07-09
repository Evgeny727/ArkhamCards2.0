package com.arkhamcards.v2.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

internal fun Modifier.applyScaffoldPaddings(paddingValues: PaddingValues) =
    padding(
        top = paddingValues.calculateTopPadding(),
        bottom = paddingValues.calculateBottomPadding()
    )