package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamSquareButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    detail: String? = null,
    enabled: Boolean = true,
    colors: ArkhamButtonColor = ArkhamButtonColor.Default,
    icon: (@Composable (Color) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val palette = buttonColors(colors)

    Surface(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(
                color = palette.ripple
            ),
            enabled = enabled,
            onClick = onClick
        ),
        color = palette.background,
        shape = if (colors == ArkhamButtonColor.DarkGray || colors == ArkhamButtonColor.LightGray)
            CustomTheme.shapes.large else CustomTheme.shapes.small,
        border = if (colors == ArkhamButtonColor.LightGrayOutline)
            BorderStroke(1.dp, CustomTheme.colors.l10)
        else
            null,
        shadowElevation = if (colors == ArkhamButtonColor.DarkGray) 8.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (icon != null) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon(palette.icon)
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (enabled) palette.text
                        else palette.disabledText,
                    style = if (detail == null) CustomTheme.typography.cardName
                        else CustomTheme.typography.large
                )

                detail?.let {
                    Text(
                        text = it,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = palette.detail,
                        style = CustomTheme.typography.smallButtonLabel
                    )
                }
            }
        }
    }
}

enum class ArkhamButtonColor {
    Default,
    Red,
    RedOutline,
    Gold,
    LightGray,
    DarkGray,
    LightGrayOutline,
}

@Immutable
private data class ArkhamButtonColors(
    val background: Color,
    val ripple: Color,
    val icon: Color,
    val text: Color,
    val detail: Color,
    val disabledText: Color,
)

@Composable
private fun buttonColors(type: ArkhamButtonColor): ArkhamButtonColors =
    when (type) {
        ArkhamButtonColor.Default -> ArkhamButtonColors(
            background = CustomTheme.colors.d10,
            ripple = CustomTheme.colors.m,
            icon = CustomTheme.colors.l10,
            text = CustomTheme.colors.l30,
            detail = CustomTheme.colors.l30,
            disabledText = CustomTheme.colors.l10
        )
        ArkhamButtonColor.Red -> ArkhamButtonColors(
            background = CustomTheme.colors.warn,
            ripple = CustomTheme.colors.faction.survivor.lightBackground,
            icon = Color.White,
            text = CustomTheme.colors.l30,
            detail = CustomTheme.colors.l30,
            disabledText = CustomTheme.colors.l30
        )
        ArkhamButtonColor.RedOutline -> ArkhamButtonColors(
            background = CustomTheme.colors.d30,
            ripple = CustomTheme.colors.d10,
            icon = CustomTheme.colors.warn,
            text = CustomTheme.colors.l30,
            detail = CustomTheme.colors.l30,
            disabledText = CustomTheme.colors.l10
        )
        ArkhamButtonColor.Gold -> ArkhamButtonColors(
            background = CustomTheme.colors.upgrade,
            ripple = CustomTheme.colors.faction.dual.lightBackground,
            icon = CustomTheme.colors.d20,
            text = CustomTheme.colors.d30,
            detail = CustomTheme.colors.d30,
            disabledText = CustomTheme.colors.d10
        )
        ArkhamButtonColor.LightGray -> ArkhamButtonColors(
            background = CustomTheme.colors.l20,
            ripple = CustomTheme.colors.l30,
            icon = CustomTheme.colors.m,
            text = CustomTheme.colors.d20,
            detail = CustomTheme.colors.d10,
            disabledText = CustomTheme.colors.d10
        )
        ArkhamButtonColor.LightGrayOutline -> ArkhamButtonColors(
            background = Color.Black,
            ripple = CustomTheme.colors.l30,
            icon = CustomTheme.colors.m,
            text = CustomTheme.colors.d20,
            detail = CustomTheme.colors.d10,
            disabledText = CustomTheme.colors.d10
        )
        ArkhamButtonColor.DarkGray -> ArkhamButtonColors(
            background = CustomTheme.colors.l10,
            ripple = CustomTheme.colors.l20,
            icon = CustomTheme.colors.d10,
            text = CustomTheme.colors.d20,
            detail = CustomTheme.colors.d10,
            disabledText = CustomTheme.colors.d10
        )
    }

internal fun iconSize(icon: Any): TextUnit = when (icon) {
    AppIcon.Wild -> 24
    AppIcon.Trauma -> 32
    AppIcon.PlusButton -> 28
    AppIcon.MinusButton -> 28
    AppIcon.RightArrow -> 32
    AppIcon.CheckThin -> 30
    AppIcon.Weakness -> 24
    AppIcon.CardOutline -> 28
    PackIcon.fromPackCode("tdea") -> 28
    PackIcon.fromPackCode("tdeb") -> 28
    AppIcon.Book -> 22
    AppIcon.Draw -> 24
    AppIcon.Arkhamdb -> 24
    AppIcon.Logo -> 28
    AppIcon.Seal -> 26
    AppIcon.Edit -> 28
    AppIcon.Upgrade -> 28
    AppIcon.Dismiss -> 28
    else -> 28
}.sp