package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.ui.components.ArkhamButton
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont

fun LazyListScope.cardDetailsDeckbuildingSection(
    onShowBaseInvestigatorCards: () -> Unit,
    onShowParallelInvestigatorCards: (() -> Unit)? = null,
    isBase: Boolean = true,
    onCreateNewDeck: () -> Unit
) {
    item("deckbuilding_header", contentType = "header") {
        CardDetailsSectionHeader(
            title = stringResource(R.string.deckbuilding),
            normalCase = false
        )
    }
    if (isBase) {
        item("base_investigator_cards_button", contentType = "button") {
            ArkhamButton(
                title = stringResource(R.string.show_all_available_cards),
                onClick = onShowBaseInvestigatorCards,
            ) { color ->
                Text(
                    text = AppIcon.Cards.glyph,
                    fontFamily = AppIconsFont,
                    color = color,
                    fontSize = 24.sp
                )
            }
        }
        onShowParallelInvestigatorCards?.let {
            item("parallel_investigator_cards_button", contentType = "button") {
                ArkhamButton(
                    title = stringResource(R.string.show_all_available_cards_for_parallel),
                    onClick = it,
                ) { color ->
                    Text(
                        text = AppIcon.Parallel1.glyph,
                        fontFamily = AppIconsFont,
                        color = color,
                        fontSize = 24.sp
                    )
                }
            }
        }
    } else {
        onShowParallelInvestigatorCards?.let {
            item("parallel_investigator_cards_button", contentType = "button") {
                ArkhamButton(
                    title = stringResource(R.string.show_all_available_cards),
                    onClick = it,
                ) { color ->
                    Text(
                        text = AppIcon.Parallel1.glyph,
                        fontFamily = AppIconsFont,
                        color = color,
                        fontSize = 24.sp
                    )
                }
            }
        }
        item("base_investigator_cards_button", contentType = "button") {
            ArkhamButton(
                title = stringResource(R.string.show_all_available_cards_for_base),
                onClick = onShowBaseInvestigatorCards,
            ) { color ->
                Text(
                    text = AppIcon.Cards.glyph,
                    fontFamily = AppIconsFont,
                    color = color,
                    fontSize = 24.sp
                )
            }
        }
    }
    item("create_deck_button", contentType = "button") {
        ArkhamButton(
            title = stringResource(R.string.create_new_deck),
            onClick = onCreateNewDeck,
        ) { color ->
            Text(
                text = AppIcon.Deck.glyph,
                fontFamily = AppIconsFont,
                color = color,
                fontSize = 24.sp
            )
        }
    }
}