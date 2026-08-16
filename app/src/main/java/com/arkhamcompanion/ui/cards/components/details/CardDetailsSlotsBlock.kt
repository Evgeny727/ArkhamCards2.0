package com.arkhamcompanion.ui.cards.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhamcompanion.ui.components.ArkhamIconText
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.theme.CustomTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CardDetailsSlotsBlock(
    slots: String,
    modifier: Modifier = Modifier
) {
    val slotList = remember(slots) {
        slots.split(".")
            .map(String::trim)
            .toImmutableList()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        slotList.forEach {
            CardDetailsSlotIcon(it)
        }
    }
}

@Composable
private fun CardDetailsSlotIcon(
    slot: String
) {
    val slotIconCode = getSlotIconCode(slot)
    val iconSize = getSlotIconSize(slot)
    val padding = when (slotIconCode) {
        "accessory" -> 4.dp
        "ally" -> 2.dp
        else -> 0.dp
    }

    Box(
        modifier = Modifier
            .size(38.dp)
            .background(
                color = CustomTheme.colors.l10,
                shape = CustomTheme.shapes.circle
            ),
        contentAlignment = Alignment.Center
    ) {
        AppIcon.fromNameCode(slotIconCode)?.let {
            ArkhamIconText(
                iconGlyph = it,
                size = iconSize.dp,
                color = CustomTheme.colors.d30,
                modifier = Modifier.padding(start = padding)
            )
        }
        AppIcon.fromNameCode("${slotIconCode}_inverted")?.let {
            ArkhamIconText(
                iconGlyph = it,
                size = iconSize.dp,
                color = CustomTheme.colors.l30,
                modifier = Modifier.padding(start = padding)
            )
        }
    }
}

@Stable
private fun getSlotIconCode(slot: String) = when (slot) {
    "Hand" -> "hand"
    "Hand x2" -> "hand_x2"
    "Accessory" -> "accessory"
    "Ally" -> "ally"
    "Arcane" -> "arcane"
    "Arcane x2" -> "arcane_x2"
    "Head" -> "head"
    "Body" -> "body"
    "Tarot" -> "tarot"
    else -> ""
}

@Stable
fun getSlotIconSize(slot: String) = when (slot) {
    "Hand" -> 22
    "Hand x2" -> 20
    "Ally" -> 24
    "Accessory", "Arcane x2" -> 28
    "Arcane", "Head", "Body", "Tarot" -> 26
    else -> 24
}