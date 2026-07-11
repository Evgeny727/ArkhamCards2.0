package com.arkhamcards.v2.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamCheckCircle(
    value: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    large: Boolean = false,
    style: CheckCircleStyle = CheckCircleStyle.Default,
    disabledColor: Color? = null,
    isRadio: Boolean = false,
    onValueChange: (Boolean) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .toggleable(
                value = value,
                enabled = enabled,
            ) { newValue ->
                haptics.performHapticFeedback(
                    if (value)
                        HapticFeedbackType.ToggleOn
                    else
                        HapticFeedbackType.ToggleOff
                )

                onValueChange(newValue)
            },
        contentAlignment = Alignment.Center
    ) {
        val icon = if (large) AppIcon.CircleThin else AppIcon.CheckCircle
        val iconSize = if (large) 34.sp else 28.sp
        val checkSize = if (large) 28.sp else 22.sp
        val checkOffset = if (large) {
            DpOffset(3.dp, (-4).dp)
        } else {
            DpOffset(2.dp, (-3).dp)
        }

        Text(
            text = icon.glyph,
            fontFamily = icon.fontFamily,
            fontSize = iconSize,
            color = with(CustomTheme.colors) {
                when {
                    enabled -> when (style) {
                        CheckCircleStyle.Default -> l10
                        CheckCircleStyle.Light -> l15
                        CheckCircleStyle.Dark -> d10
                    }
                    else -> disabledColor ?: l20
                }
            }
        )

        if (isRadio && value) Box(modifier = Modifier.size(18.dp).background(
            color = if (enabled) CustomTheme.colors.m else disabledColor ?: CustomTheme.colors.l15,
            shape = CustomTheme.shapes.circle
        )) else Text(
            text = AppIcon.Check.glyph,
            fontFamily = AppIcon.Check.fontFamily,
            fontSize = checkSize,
            color = with(CustomTheme.colors) {
                when {
                    enabled -> when (style) {
                        CheckCircleStyle.Default -> m
                        CheckCircleStyle.Light -> l30
                        CheckCircleStyle.Dark -> d20
                    }
                    else -> disabledColor ?: l15
                }
            },
            modifier = Modifier.offset(checkOffset.x, checkOffset.y)
                .alpha(
                    animateFloatAsState(
                        if (value) 1f else 0f,
                    ).value
                )
        )
    }
}

enum class CheckCircleStyle {
    Default,
    Light,
    Dark
}