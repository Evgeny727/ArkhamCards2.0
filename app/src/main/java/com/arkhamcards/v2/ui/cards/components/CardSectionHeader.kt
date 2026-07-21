package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.model.cards.CardsHeaderType
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun CardSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = CustomTheme.colors.l20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.subHeaderText,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@ReadOnlyComposable
@Composable
internal fun buildHeaderTitle(
    headerType: CardsHeaderType?,
    value: Any?,
): String {
    val colon = CustomTheme.language.colon
    return when(headerType) {
        CardsHeaderType.TYPE -> (value as? String) ?: stringResource(R.string.unknown)
        CardsHeaderType.TYPE_SLOT -> {
            val pair = (value as? Pair<*, *>) ?: return stringResource(R.string.unknown)
            val slotText = if (pair.second == null) ""
                else (colon + getSlotBySortNumber(pair.second.toString()))

            pair.first.toString() + slotText
        }
        CardsHeaderType.SLOT -> getSlotBySortNumber(value.toString())
        CardsHeaderType.FACTION -> getFactionBySortNumber(value as? String)
        CardsHeaderType.FACTION_PACK -> ""
        CardsHeaderType.FACTION_LEVEL -> ""
        CardsHeaderType.COST -> {
            (value as? Int)?.let {
                stringResource(R.string.cost_value, if (it == -2) "X" else it)
            } ?: stringResource(R.string.cost_none)
        }
        CardsHeaderType.LEVEL -> {
            (value as? Int)?.let {
                stringResource(R.string.level_level, it)
            } ?: stringResource(R.string.none)
        }
        CardsHeaderType.CYCLE -> (value as? String) ?: stringResource(R.string.unknown)
        CardsHeaderType.PACK -> (value as? String) ?: stringResource(R.string.unknown)
        CardsHeaderType.PACK_ENCOUNTER_SET -> (value as? String)
            ?.replaceFirst(":", colon) ?: stringResource(R.string.unknown)
        CardsHeaderType.POSITION -> (value as? String) ?: stringResource(R.string.unknown)
        CardsHeaderType.ENCOUNTER_SET -> (value as? String) ?: stringResource(R.string.unknown)
        else -> stringResource(R.string.all_cards)
    }
}

@ReadOnlyComposable
@Composable
private fun getSlotBySortNumber(slotNumber: String): String {
    val intSlotNumber = slotNumber.toIntOrNull()
    return if (intSlotNumber == null) slotNumber else stringResource(when (intSlotNumber) {
        0 -> R.string.none
        1 -> R.string.hand
        2 -> R.string.hand_arcane
        3 -> R.string.hand_x2
        4 -> R.string.hand_x2_arcane
        5 -> R.string.accessory
        6 -> R.string.ally
        7 -> R.string.ally_arcane
        8 -> R.string.arcane
        9 -> R.string.arcane_x2
        10 -> R.string.arcane_accessory
        11 -> R.string.head
        12 -> R.string.body
        13 -> R.string.body_arcane
        14 -> R.string.body_hand_x2
        15 -> R.string.tarot
        99 -> R.string.permanent
        100 -> R.string.other
        else -> R.string.unknown
    })
}

@ReadOnlyComposable
@Composable
private fun getFactionBySortNumber(factionNumber: String?): String {
    return stringResource(when (factionNumber) {
        "0" -> R.string.guardian
        "1" -> R.string.seeker
        "2" -> R.string.rogue
        "3" -> R.string.mystic
        "4" -> R.string.survivor
        "5" -> R.string.neutral
        "6" -> R.string.multiclass
        else -> R.string.mythos
    })
}