package com.arkhamcards.v2.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.ui.cards.components.CardDetailsHeader
import com.arkhamcards.v2.ui.cards.components.CardDetailsSectionHeader
import com.arkhamcards.v2.ui.cards.components.cardDetailsDeckbuildingSection
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings

@Composable
fun CardDetailsScreen(
    cardCode: String,
    cardsViewModel: CardsViewModel,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    val cardsLazyCodes by cardsViewModel.searchResultCodes.collectAsState()
    val index = remember(cardsLazyCodes) {
        cardsLazyCodes.indexOfFirst { it.code == cardCode }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(initialPage = index) { cardsLazyCodes.size }

    LaunchedEffect(cardsLazyCodes) {
        if (cardsLazyCodes.isNotEmpty()) {
            pagerState.scrollToPage(index)
        }
    }

    HorizontalPager(
        state = pagerState,
        key = { page -> cardsLazyCodes[page].code },
        modifier = modifier
            .fillMaxSize()
            .applyScaffoldPaddings(innerPadding),
    ) { page ->
        val item = cardsLazyCodes[page]
        val cardDetailsWithRelations by cardsViewModel.getCardDetailsWithRelations(
            item.code,
            item.tabooSetId
        ).collectAsState(null)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 32.dp
            )
        ) {
            if (cardDetailsWithRelations == null) item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = CustomTheme.colors.m)
                }
            } else {
                val isInvestigator = cardDetailsWithRelations?.cardDetails?.cardDetails?.type == CardType.Investigator
                val isParallel = cardDetailsWithRelations?.cardRelations?.parallel != null
                val isBase = cardDetailsWithRelations?.cardRelations?.base != null

                cardDetailsWithRelations?.cardDetails?.run {
                    item(
                        key = cardDetails.id,
                        contentType = "card_details"
                    ) {
                        ArkhamRoundedFactionCard(
                            faction = if (cardDetails.faction2 != null) Faction.Dual
                                else cardDetails.faction,
                            modifier = Modifier.fillMaxWidth(),
                            header = {
                                CardDetailsHeader(cardDetails)
                            }
                        ) { }
                    }
                }

                if (isInvestigator) {
                    if (isParallel) {
                        item("parallel_investigator_header", contentType = "header") {
                            CardDetailsSectionHeader(
                                title = stringResource(R.string.parallel_investigator),
                                normalCase = false
                            )
                        }

                        //TODO:Show parallel investigator
                        cardDetailsWithRelations?.cardRelations?.parallel?.run {
                            item(
                                key = "parallel_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                        else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }

                    if (isBase) {
                        item("base_investigator_header", contentType = "header") {
                            CardDetailsSectionHeader(
                                title = stringResource(R.string.base_investigator),
                                normalCase = false
                            )
                        }

                        //TODO:Show base investigator
                        cardDetailsWithRelations?.cardRelations?.base?.run {
                            item(
                                key = "base_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                        else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }

                    if (!cardDetailsWithRelations?.cardRelations?.otherVersions.isNullOrEmpty()) {
                        item("other_versions_header", contentType = "header") {
                            CardDetailsSectionHeader(
                                title = stringResource(R.string.other_versions),
                                normalCase = false
                            )
                        }

                        cardDetailsWithRelations?.cardRelations?.otherVersions?.forEach { otherVersion ->
                            otherVersion.run {
                                item(
                                    key = "other_version_${cardDetails.id}",
                                    contentType = "card_details"
                                ) {
                                    ArkhamRoundedFactionCard(
                                        faction = if (cardDetails.faction2 != null) Faction.Dual
                                            else cardDetails.faction,
                                        modifier = Modifier.fillMaxWidth(),
                                        header = {
                                            CardDetailsHeader(cardDetails)
                                        }
                                    ) { }
                                }
                            }
                        }
                    }

                    if (cardDetailsWithRelations?.cardDetails?.cardDetails?.encounterCode == null) {
                        cardDetailsDeckbuildingSection(
                            onShowBaseInvestigatorCards = { /*TODO:Show base cardpool*/ },
                            onShowParallelInvestigatorCards = if (isParallel || isBase) { {
                                /*TODO:Show parallel cardpool*/
                            } } else null,
                            isBase = !isBase
                        ) {
                            /*TODO:Create new deck*/
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.requiredCards.isNullOrEmpty()) {
                    item("required_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.required_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.requiredCards?.forEach { required ->
                        required.run {
                            item(
                                key = "required_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.sideDeckRequiredCards.isNullOrEmpty()) {
                    item("side_required_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.side_required_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.sideDeckRequiredCards?.forEach { sideRequired ->
                        sideRequired.run {
                            item(
                                key = "side_required_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.advanced.isNullOrEmpty()) {
                    item("advanced_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.advanced_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.advanced?.forEach { advanced ->
                        advanced.run {
                            item(
                                key = "advanced_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.replacement.isNullOrEmpty()) {
                    item("replacement_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.replacement_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.replacement?.forEach { replacement ->
                        replacement.run {
                            item(
                                key = "replacement_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.parallelCards.isNullOrEmpty()) {
                    item("parallel_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.parallel_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.parallelCards?.forEach { parallel ->
                        parallel.run {
                            item(
                                key = "parallel_card_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.otherSignatures.isNullOrEmpty()) {
                    item("other_signature_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.other_signature_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.otherSignatures?.forEach { otherSignature ->
                        otherSignature.run {
                            item(
                                key = "other_signature_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.bound.isNullOrEmpty()) {
                    item("bound_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.bound_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.bound?.forEach { bound ->
                        bound.run {
                            item(
                                key = "bound_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.bonded.isNullOrEmpty()) {
                    item("bonded_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.bonded),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.bonded?.forEach { bonded ->
                        bonded.run {
                            item(
                                key = "bonded_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }

                if (!cardDetailsWithRelations?.cardRelations?.level.isNullOrEmpty()) {
                    item("level_cards_header", contentType = "header") {
                        CardDetailsSectionHeader(
                            title = stringResource(R.string.other_level_cards),
                            normalCase = false
                        )
                    }

                    cardDetailsWithRelations?.cardRelations?.level?.forEach { level ->
                        level.run {
                            item(
                                key = "level_${cardDetails.id}",
                                contentType = "card_details"
                            ) {
                                ArkhamRoundedFactionCard(
                                    faction = if (cardDetails.faction2 != null) Faction.Dual
                                    else cardDetails.faction,
                                    modifier = Modifier.fillMaxWidth(),
                                    header = {
                                        CardDetailsHeader(cardDetails)
                                    }
                                ) { }
                            }
                        }
                    }
                }
            }
        }
    }
}