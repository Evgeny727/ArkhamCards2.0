package com.arkhamcards.v2.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamSwitch(value: Boolean, onValueChange: (Boolean) -> Unit) {
    val investigatorIcon = AppIcon.PerInvestigator
    val mythosIcon = AppIcon.AutoFail

    // Define dimensions for the switch
    val switchWidth = 76.dp
    val switchHeight = 36.dp
    val indicatorSize = 32.dp
    val padding = 2.dp

    // Get the current density for converting Dp to pixels.
    val density = LocalDensity.current

    // Determine the target offset in Dp based on the switch state.
    val targetOffsetDp = if (value) switchWidth - indicatorSize - padding else padding
    // Convert the Dp value to pixels as an Int.
    val targetOffsetPx = with(density) { targetOffsetDp.toPx().toInt() }

    // Animate the IntOffset for the indicator using animateIntOffsetAsState.
    val animatedOffset by animateIntOffsetAsState(
        targetValue = IntOffset(targetOffsetPx, 0),
        animationSpec = tween(durationMillis = 250),
        label = "offset"
    )

    val animatedColorInvestigatorIcon by animateColorAsState(
        targetValue = if (value) CustomTheme.colors.l10 else CustomTheme.colors.d30,
        label = "Color for investigator icon"
    )
    val animatedColorMythosIcon by animateColorAsState(
        targetValue = if (value) CustomTheme.colors.d30 else CustomTheme.colors.l10,
        label = "Color for mythos icon"
    )

    Box(
        modifier = Modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(CustomTheme.shapes.circle)
            .background(CustomTheme.colors.background)
            .border(1.dp, CustomTheme.colors.l10, CustomTheme.shapes.circle)
            .toggleable(value = value, onValueChange = onValueChange),
        contentAlignment = Alignment.CenterStart
    ) {
        // The animated circular indicator that moves behind the active icon.
        Box(
            modifier = Modifier
                .size(indicatorSize)
                .offset{ animatedOffset }
                .clip(CustomTheme.shapes.circle)
                .background(CustomTheme.colors.l10)
        )
        // Place the icons on top of the background. They are always visible.
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = investigatorIcon.glyph,
                fontFamily = investigatorIcon.fontFamily,
                fontSize = 24.sp,
                color = animatedColorInvestigatorIcon
            )
            Text(
                text = mythosIcon.glyph,
                fontFamily = mythosIcon.fontFamily,
                fontSize = 24.sp,
                color = animatedColorMythosIcon
            )
        }
    }
}