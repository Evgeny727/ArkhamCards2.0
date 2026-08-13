package com.arkhamcompanion.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.theme.AppIconsFont
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
        Text(
            text = AppIcon.fromNameCode("skill_${skillCode}_inverted")?.glyph.toString(),
            fontFamily = AppIconsFont,
            fontSize = iconSize,
            color = Color.White,
        )

        Text(
            text = AppIcon.fromNameCode("skill_$skillCode")?.glyph.toString(),
            fontFamily = AppIconsFont,
            fontSize = iconSize,
            color = if (isWeakness) Color.Black else getSkillIconColor(skillCode)
        )
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
            if (withBackground) Text(
                text = AppIcon.HealthInverted.glyph,
                fontFamily = AppIconsFont,
                fontSize = iconSize,
                color = Color.White,
            )

            Text(
                text = AppIcon.Health.glyph,
                fontFamily = AppIconsFont,
                fontSize = iconSize,
                color = CustomTheme.colors.health
            )
        } else {
            if (withBackground) Text(
                text = AppIcon.SanityInverted.glyph,
                fontFamily = AppIconsFont,
                fontSize = iconSize,
                color = Color.White,
            )

            Text(
                text = AppIcon.Sanity.glyph,
                fontFamily = AppIconsFont,
                fontSize = iconSize,
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
            Text(
                text = AppIcon.fromNameCode("$firstNumberIcon-fill")?.glyph.toString(),
                fontFamily = AppIconsFont,
                fontSize = iconSize,
                color = Color.White
            )
            Text(
                text = AppIcon.fromNameCode("$firstNumberIcon-outline")?.glyph.toString(),
                fontFamily = AppIconsFont,
                fontSize = iconSize,
                color = color
            )
        }

        secondNumberIcon?.let {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = AppIcon.fromNameCode("$it-fill")?.glyph.toString(),
                    fontFamily = AppIconsFont,
                    fontSize = iconSize,
                    color = Color.White
                )
                Text(
                    text = AppIcon.fromNameCode("$it-outline")?.glyph.toString(),
                    fontFamily = AppIconsFont,
                    fontSize = iconSize,
                    color = color
                )
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