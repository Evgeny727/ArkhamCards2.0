package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.CardType.Companion.isLocationLike
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardDetails
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithPackInfo
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard

fun LazyListScope.doubleSidedCardDetails(
    cardDetailsWithPackInfo: CardDetailsWithPackInfo,
    prefix: String,
    collection: Collection,
    suffix: String = ""
) {
    cardDetailsWithPackInfo.run {
        val isBackFirst = cardDetails.isBackFirst()

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
                    header = {
                        CardDetailsHeader(
                            cardDetails,
                            firstPackInCollection = firstPackIn(collection)
                        )
                    }
                ) { }
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
                header = if (isBackFirst) null else { {
                    CardDetailsHeader(
                        cardDetails,
                        firstPackInCollection = firstPackIn(collection)
                    )
                } }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardDetailsMetaBlock(cardDetails)

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
                ) { }
            }
        }
    }
}

private fun CardDetails.isBackFirst(): Boolean {
    return doubleSided && isLocationLike(type) && encounterCode != null
}