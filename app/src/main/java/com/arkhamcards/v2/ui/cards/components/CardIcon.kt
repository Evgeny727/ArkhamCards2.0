package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.IconGlyph
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.FactionColors
import com.arkhamcards.v2.ui.utils.appSp

internal fun iconScaleFactor(scaleFactor: Float) = (scaleFactor - 1) / 2 + 1

@Composable
fun CardIcon(
    xp: Int?,
    type: CardType,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    factionColor: FactionColors,
    cost: String?,
    packCode: String,
    encounterCode: String?,
    inverted: Boolean = false
) {
    val showCost = type == CardType.Asset || type == CardType.Event || type == CardType.Skill
    val iconScaleFactor = iconScaleFactor(CustomTheme.typography.scaleFactor)

    Box(contentAlignment = Alignment.Center) {
        if (showCost) CardCostIcon(xp, type, subType, faction, faction2, factionColor, cost, inverted, iconScaleFactor)
        else CardFactionIcon(type, subType,faction, faction2, factionColor, packCode, encounterCode, iconScaleFactor)
    }
}

@Composable
fun CardCostIcon(
    xp: Int?,
    type: CardType,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    factionColor: FactionColors,
    cost: String?,
    inverted: Boolean,
    iconScaleFactor: Float
) {
    val levelText = xp?.toString() ?: "none"
    val icon = AppIcon.fromNameCode((if (inverted) "inverted_" else "") + "level_$levelText")
    if (!inverted) {
        val invertedIcon = AppIcon.fromNameCode("inverted_level_$levelText")
        Text(
            text = invertedIcon.glyph,
            fontFamily = invertedIcon.fontFamily,
            fontSize = 32.appSp(iconScaleFactor),
            color = CustomTheme.colors.background,
        )
    }
    Text(
        text = icon.glyph,
        fontFamily = icon.fontFamily,
        fontSize = 32.appSp(iconScaleFactor),
        color = if (inverted) Color.White else factionColor.text,
    )
    if (type == CardType.Skill) {
        val factionIcon = factionIcon(faction, faction2, subType)
        Text(
            text = factionIcon.glyph,
            fontFamily = factionIcon.fontFamily,
            fontSize = 18.appSp(iconScaleFactor),
            color = if (inverted) Color.White else CustomTheme.colors.background,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    } else {
        val digitCost = if (cost?.isDigitsOnly() == true) cost.toInt() else -1
        val costIcon = when {
            cost == null || cost == "-" -> AppIcon.Numnull.glyph
            cost == "X" -> AppIcon.X.glyph
            digitCost >= 0 -> {
                if (digitCost <= 9) AppIcon.fromNameCode("num$digitCost").glyph
                else {
                    val firstDigit = digitCost / 10
                    val secondDigit = digitCost % 10
                    AppIcon.fromNameCode("num$firstDigit").glyph + AppIcon.fromNameCode("num$secondDigit").glyph
                }
            }
            else -> AppIcon.Star.glyph
        }
        Text(
            text = costIcon,
            fontFamily = AppIconsFont,
            fontSize = (if (digitCost >= 10) 14 else 18).appSp(iconScaleFactor),
            color = if (inverted) Color.White else CustomTheme.colors.background,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }
}

@Composable
fun CardFactionIcon(
    type: CardType,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    factionColor: FactionColors,
    packCode: String,
    encounterCode: String?,
    iconScaleFactor: Float
) {
    if (encounterCode != null) {
        EncounterIcon(
            iconCode = encounterCode,
            iconSize = 32.appSp(iconScaleFactor),
            iconColor = CustomTheme.colors.darkText,
        )
    } else if (subType == CardSubType.BasicWeakness || subType == CardSubType.Weakness) {
        Text(
            text = AppIcon.Weakness.glyph,
            fontFamily = AppIconsFont,
            fontSize = 32.appSp(iconScaleFactor),
            color = CustomTheme.colors.faction.neutral.text,
        )
    } else if (type == CardType.Scenario || type == CardType.Story) {
        EncounterIcon(
            iconCode = packCode,
            iconSize = 32.appSp(iconScaleFactor),
            iconColor = CustomTheme.colors.darkText
        )
    } else {
        val factionIcon = factionIcon(faction, faction2, null)
        Text(
            text = factionIcon.glyph,
            fontFamily = factionIcon.fontFamily,
            fontSize = 32.appSp(iconScaleFactor),
            color = factionColor.text,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Composable
fun EncounterIcon(iconCode: String, iconSize: TextUnit, iconColor: Color, isPack: Boolean = false) {
    val icon = PackIcon.fromPackCode(iconCode, isPack)
    Text(
        text = icon.glyph,
        fontFamily = icon.fontFamily,
        fontSize = iconSize,
        color = iconColor,
    )
}

internal fun factionIcon(faction: Faction, faction2: Faction? = null, subType: CardSubType? = null): IconGlyph = when {
    faction2 != null -> AppIcon.Multiclass
    subType == CardSubType.BasicWeakness -> AppIcon.Weakness
    subType == CardSubType.Weakness -> AppIcon.Weakness
    else -> when (faction) {
        Faction.Mystic -> AppIcon.Mystic
        Faction.Seeker -> AppIcon.Seeker
        Faction.Rogue -> AppIcon.Rogue
        Faction.Guardian -> AppIcon.Guardian
        Faction.Survivor -> AppIcon.Survivor
        else -> AppIcon.Neutral
    }
}