package com.arkhamcompanion.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.arkhamcompanion.ui.components.ArkhamScalableIconText
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.theme.CustomTheme

@Composable
fun SkillIcon(
    skillCode: String,
    iconSize: TextUnit,
    modifier: Modifier = Modifier,
    isWeakness: Boolean = false,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AppIcon.fromNameCode("skill_${skillCode}_inverted")?.let {
            ArkhamScalableIconText(
                iconGlyph = it,
                size = iconSize,
                color = Color.White,
            )
        }

        AppIcon.fromNameCode("skill_$skillCode")?.let {
            ArkhamScalableIconText(
                iconGlyph = it,
                size = iconSize,
                color = if (isWeakness) Color.Black else getSkillIconColor(skillCode)
            )
        }
    }
}

@Composable
fun HealthSanityIcon(
    isHealth: Boolean,
    iconSize: TextUnit,
    modifier: Modifier = Modifier,
    withBackground: Boolean = true,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isHealth) {
            if (withBackground) ArkhamScalableIconText(
                iconGlyph = AppIcon.HealthInverted,
                size = iconSize,
                color = Color.White,
            )

            ArkhamScalableIconText(
                iconGlyph = AppIcon.Health,
                size = iconSize,
                color = CustomTheme.colors.health
            )
        } else {
            if (withBackground) ArkhamScalableIconText(
                iconGlyph = AppIcon.SanityInverted,
                size = iconSize,
                color = Color.White,
            )

            ArkhamScalableIconText(
                iconGlyph = AppIcon.Sanity,
                size = iconSize,
                color = CustomTheme.colors.sanity
            )
        }
    }
}

@ReadOnlyComposable
@Composable
private fun getSkillIconColor(skillCode: String) = with(CustomTheme.colors.skill) {
    return@with when (skillCode) {
        "willpower" -> willpower
        "intellect" -> intellect
        "combat" -> combat
        "agility" -> agility
        else -> wild
    }
}

@Composable
fun HealthSanityNumericIcon(
    value: Int?,
    isHealth: Boolean,
    iconSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    val (firstNumberIcon, secondNumberIcon) = if ((value ?: 0) > 9) {
        val firstNumber = value?.div(10)
        val secondNumber = value?.rem(10)

        getNumberIconCode(firstNumber) to getNumberIconCode(secondNumber)
    } else {
        getNumberIconCode(value) to null
    }
    val color = if (isHealth) CustomTheme.colors.health else CustomTheme.colors.sanity

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy((-8).dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AppIcon.fromNameCode("$firstNumberIcon-fill")?.let {
                ArkhamScalableIconText(
                    iconGlyph = it,
                    size = iconSize,
                    color = Color.White,
                )
            }

            AppIcon.fromNameCode("$firstNumberIcon-outline")?.let {
                ArkhamScalableIconText(
                    iconGlyph = it,
                    size = iconSize,
                    color = color
                )
            }
        }

        secondNumberIcon?.let {
            Box(contentAlignment = Alignment.Center) {
                AppIcon.fromNameCode("$it-fill")?.let { icon ->
                    ArkhamScalableIconText(
                        iconGlyph = icon,
                        size = iconSize,
                        color = Color.White,
                    )
                }

                AppIcon.fromNameCode("$it-outline")?.let { icon ->
                    ArkhamScalableIconText(
                        iconGlyph = icon,
                        size = iconSize,
                        color = color
                    )
                }
            }
        }
    }
}

private fun getNumberIconCode(value: Int?) = when (value) {
    null -> "numNull"
    -2 -> "x"
    -3 -> "star"
    else -> "num$value"
}