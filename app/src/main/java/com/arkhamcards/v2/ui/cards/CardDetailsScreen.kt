package com.arkhamcards.v2.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.model.cards.CodeWithTaboo
import com.arkhamcards.v2.ui.cards.components.details.cardDetailsDeckbuildingSection
import com.arkhamcards.v2.ui.cards.components.details.cardDetailsRelationSection
import com.arkhamcards.v2.ui.cards.components.details.cardDetailsRelationSectionSingle
import com.arkhamcards.v2.ui.cards.components.details.doubleSidedCardDetails
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CardDetailsScreen(
    cardCode: String,
    cardCodes: ImmutableList<CodeWithTaboo>,
    cardDetailsViewModel: CardDetailsViewModel,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    val collection by cardDetailsViewModel.collectionFlow.collectAsState()
    val ignoreCollection by cardDetailsViewModel.ignoreCollectionFlow.collectAsState()
    val showFanmade by cardDetailsViewModel.showFanmadeFlow.collectAsState()
    val index = remember(cardCodes) {
        cardCodes.indexOfFirst { it.code == cardCode }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(initialPage = index) { cardCodes.size }

    LaunchedEffect(cardCodes) {
        if (cardCodes.isNotEmpty()) {
            pagerState.scrollToPage(index)
        }
    }

    HorizontalPager(
        state = pagerState,
        key = { page -> cardCodes[page].code },
        modifier = modifier
            .fillMaxSize()
            .applyScaffoldPaddings(innerPadding),
    ) { page ->
        val item = cardCodes[page]
        val cardDetailsWithRelations by cardDetailsViewModel.getCardDetailsWithRelations(
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
                val isInvestigator = cardDetailsWithRelations?.card?.details?.cardDetails?.type ==
                        CardType.Investigator

                val isParallel = cardDetailsWithRelations?.cardRelations?.parallel != null
                val isBase = cardDetailsWithRelations?.cardRelations?.base != null

                cardDetailsWithRelations?.card?.let { relatedCard ->
                    doubleSidedCardDetails(relatedCard, "main", collection)
                }

                if (isInvestigator) {
                    if (isParallel) {
                        cardDetailsWithRelations?.cardRelations?.parallel?.let { relatedCard ->
                            cardDetailsRelationSectionSingle(
                                relatedCard = relatedCard,
                                prefix = "parallel",
                                sectionTitleResId = R.string.parallel_investigator,
                                collection = collection,
                                showFanmade = showFanmade,
                                ignoreCollection = ignoreCollection
                            )
                        }
                    }

                    if (isBase) {
                        cardDetailsWithRelations?.cardRelations?.base?.let { relatedCard ->
                            cardDetailsRelationSectionSingle(
                                relatedCard = relatedCard,
                                prefix = "base",
                                sectionTitleResId = R.string.base_investigator,
                                collection = collection,
                                showFanmade = showFanmade,
                                ignoreCollection = ignoreCollection
                            )
                        }
                    }

                    cardDetailsWithRelations?.cardRelations?.otherVersions.run {
                        if (isNullOrEmpty()) return@run

                        cardDetailsRelationSection(
                            relatedCards = this,
                            prefix = "other_versions",
                            sectionTitleResId = R.string.other_versions,
                            collection = collection,
                            showFanmade = showFanmade,
                            ignoreCollection = ignoreCollection
                        )
                    }

                    if (cardDetailsWithRelations?.card?.details?.cardDetails?.encounterCode == null) {
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

                cardDetailsWithRelations?.cardRelations?.restrictedTo.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "restricted_to",
                        sectionTitleResId = R.string.restricted_to,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.requiredCards.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "required",
                        sectionTitleResId = R.string.required_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.sideDeckRequiredCards.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "side_required_to",
                        sectionTitleResId = R.string.side_required_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.advanced.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "advanced",
                        sectionTitleResId = R.string.advanced_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.replacement.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "replacement",
                        sectionTitleResId = R.string.replacement_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.parallelCards.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "parallel_card",
                        sectionTitleResId = R.string.parallel_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.otherSignatures.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "other_signature",
                        sectionTitleResId = R.string.other_signature_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.bound.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "bound",
                        sectionTitleResId = R.string.bound_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.bonded.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "bonded",
                        sectionTitleResId = R.string.bonded,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }

                cardDetailsWithRelations?.cardRelations?.level.run {
                    if (isNullOrEmpty()) return@run

                    cardDetailsRelationSection(
                        relatedCards = this,
                        prefix = "level",
                        sectionTitleResId = R.string.other_level_cards,
                        collection = collection,
                        showFanmade = showFanmade,
                        ignoreCollection = ignoreCollection
                    )
                }
            }
        }
    }
}