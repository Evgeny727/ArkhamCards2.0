package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.CardType.Companion.isLocationLike
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardDetails
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithPackInfo
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.CardTextStyleResolver

fun LazyListScope.doubleSidedCardDetails(
    cardDetailsWithPackInfo: CardDetailsWithPackInfo,
    prefix: String,
    collection: Collection,
    suffix: String = "",
    styleResolver: CardTextStyleResolver,
    flavorStyleResolver: CardTextStyleResolver,
) {
    cardDetailsWithPackInfo.run {
        val firstPackInCollection = firstPackIn(collection)
        val isBackFirst = cardDetails.isBackFirst()
        val noBackHeader = cardDetails.type == CardType.Investigator

        if (isBackFirst) {
            //Back
            item(
                key = "${prefix}_${cardDetails.id}${suffix}_backside",
                contentType = "card_details"
            ) {
                ArkhamRoundedFactionCard(
                    faction = if (cardDetails.faction2 != null) Faction.Dual
                    else cardDetails.faction,
                    modifier = Modifier.fillMaxWidth(),
                    header = if (noBackHeader) null else { {
                        CardDetailsHeader(
                            cardDetails,
                            firstPackInCollection = firstPackInCollection,
                            isBack = true
                        )
                    } }
                ) {
                    CardDetailsBackContent(cardDetailsWithPackInfo, styleResolver, flavorStyleResolver)
                }
            }
        }

        //Front
        item(
            key = "${prefix}_${cardDetails.id}$suffix",
            contentType = "card_details"
        ) {
            ArkhamRoundedFactionCard(
                faction = if (cardDetails.faction2 != null) Faction.Dual
                else cardDetails.faction,
                modifier = Modifier.fillMaxWidth(),
                header = {
                    CardDetailsHeader(
                        cardDetails,
                        firstPackInCollection = firstPackInCollection
                    )
                }
            ) {
                CardDetailsFrontContent(
                    cardDetailsWithPackInfo,
                    firstPackInCollection?.code,
                    styleResolver,
                    flavorStyleResolver
                )
            }
        }

        cardDetails.parsedCustomizationText?.let { customizationText ->
            item(
                key = "${prefix}_${cardDetails.id}${suffix}_customization",
                contentType = "card_details"
            ) {
                ArkhamRoundedFactionCard(
                    faction = if (cardDetails.faction2 != null) Faction.Dual
                    else cardDetails.faction,
                    modifier = Modifier.fillMaxWidth(),
                    header = {
                        CardDetailsHeader(
                            cardDetails,
                            firstPackInCollection = null,
                            isCustomizableSheet = true
                        )
                    }
                ) {
                    ParsedCardText(customizationText, styleResolver)
                }
            }
        }

        if (!isBackFirst && cardDetails.doubleSided) {
            //Back
            item(
                key = "${prefix}_${cardDetails.id}${suffix}_backside",
                contentType = "card_details"
            ) {
                ArkhamRoundedFactionCard(
                    faction = if (cardDetails.faction2 != null) Faction.Dual
                    else cardDetails.faction,
                    modifier = Modifier.fillMaxWidth(),
                    header = if (noBackHeader) null else { {
                        CardDetailsHeader(
                            cardDetails,
                            firstPackInCollection = firstPackInCollection,
                            isBack = true
                        )
                    } }
                ) {
                    CardDetailsBackContent(cardDetailsWithPackInfo, styleResolver, flavorStyleResolver)
                }
            }
        }
    }
}

private fun CardDetails.isBackFirst(): Boolean {
    return doubleSided && isLocationLike(type) && encounterCode != null
}

@Composable
fun CardDetailsFrontContent(
    cardDetailsWithPackInfo: CardDetailsWithPackInfo,
    firstPackInCollection: String?,
    styleResolver: CardTextStyleResolver,
    flavorStyleResolver: CardTextStyleResolver
) {
    cardDetailsWithPackInfo.run {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardDetailsMetaBlock(cardDetails, flavorStyleResolver)

            cardDetails.realSlot?.let { slots ->
                CardDetailsSlotsBlock(slots)
            }

            if (cardDetails.type != CardType.Story) cardDetails.run {
                CardDetailsClickableThumbnail(
                    thumbnailUrl = thumbnailUrl,
                    imageUrl = imageUrl,
                    backImageUrl = backImageUrl,
                    taboSetId = tabooSetId,
                    type = type,
                    backType = backType,
                    encounterCode = encounterCode,
                    subType = subType,
                    faction = faction,
                    faction2 = faction2,
                )
            }
        }

        cardDetails.parsedText?.let {
            ParsedCardText(it, styleResolver)
        }

        cardDetails.victory?.let {
            Text(
                text = stringResource(R.string.victory_value, it),
                style = CustomTheme.typography.run { small + bold }
            )
        }

        cardDetails.vengeance?.let {
            Text(
                text = stringResource(R.string.vengeance_value, it),
                style = CustomTheme.typography.run { small + bold }
            )
        }

        cardDetails.run {
            if (type != CardType.Agenda && type != CardType.Act && type != CardType.Story) {
                parsedFlavor?.let {
                    ParsedCardText(it, flavorStyleResolver, isFlavor = true)
                }
            }
        }

        if (cardDetails.tabooSetId != null && !cardDetails.tabooPlaceholder) {
            CardDetailsTabooBlock(
                tabooXp = cardDetails.tabooXp,
                tabooOriginalText = cardDetails.parsedTabooOriginalText,
                tabooOriginalBackText = cardDetails.parsedTabooOriginalBackText,
                styleResolver = styleResolver,
                deckLimit = cardDetails.deckLimit ?: 0
            )
        }
    }

    CardDetailsPackInfoBlock(
        cardDetailsWithPackInfo = cardDetailsWithPackInfo,
        firstPackInCollection = firstPackInCollection
    )
}

@Composable
fun CardDetailsBackContent(
    cardDetailsWithPackInfo: CardDetailsWithPackInfo,
    styleResolver: CardTextStyleResolver,
    flavorStyleResolver: CardTextStyleResolver
) {
    cardDetailsWithPackInfo.run {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardDetailsMetaBlock(cardDetails, flavorStyleResolver, simpleBack = true)

            cardDetails.realSlot?.let { slots ->
                CardDetailsSlotsBlock(slots)
            }

            if (cardDetails.type != CardType.Investigator && cardDetails.type != CardType.Story) {
                cardDetails.run {
                    CardDetailsClickableThumbnail(
                        thumbnailUrl = thumbnailUrl,
                        //TODO: add backThumbNail
                        imageUrl = imageUrl,
                        backImageUrl = backImageUrl,
                        taboSetId = tabooSetId,
                        type = type,
                        backType = backType,
                        encounterCode = encounterCode,
                        subType = subType,
                        faction = faction,
                        faction2 = faction2,
                    )
                }
            }
        }

        cardDetails.parsedBackText?.let {
            ParsedCardText(it, styleResolver)
        }

        cardDetails.run {
            if (type != CardType.Agenda && type != CardType.Act && type != CardType.Story) {
                parsedBackFlavor?.let {
                    ParsedCardText(it, flavorStyleResolver, isFlavor = true)
                }
            }
        }

        if (cardDetails.illustrator != cardDetails.backIllustrator && cardDetails.backIllustrator != null) {
            CardDetailsPackInfoBlock(
                cardDetailsWithPackInfo = cardDetailsWithPackInfo,
                onlyIllustrator = true
            )
        }
    }
}