package com.arkhamcards.v2.ui.cards.components.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.RelatedCard
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import kotlinx.collections.immutable.ImmutableList

fun LazyListScope.cardDetailsRelationSection(
    relatedCards: ImmutableList<RelatedCard>,
    prefix: String,
    @StringRes sectionTitleResId: Int,
    collection: Collection,
    showFanmade: Boolean,
    ignoreCollection: Boolean
) {
    var headerAdded = false

    relatedCards.forEach { relatedCard ->
        if (!relatedCard.shouldShow(collection, ignoreCollection, showFanmade)) return@forEach

        if (!headerAdded) {
            headerAdded = true
            cardDetailsRelationSectionHeader(prefix, sectionTitleResId)
        }

        cardDetailsWithLinkedBack(relatedCard, prefix, collection)
    }
}

fun LazyListScope.cardDetailsRelationSectionSingle(
    relatedCard: RelatedCard,
    prefix: String,
    @StringRes sectionTitleResId: Int,
    collection: Collection,
    showFanmade: Boolean,
    ignoreCollection: Boolean
) {
    if (!relatedCard.shouldShow(collection, ignoreCollection, showFanmade)) return

    cardDetailsRelationSectionHeader(prefix, sectionTitleResId)

    cardDetailsWithLinkedBack(relatedCard, prefix, collection)
}

private fun LazyListScope.cardDetailsRelationSectionHeader(
    prefix: String,
    @StringRes sectionTitleResId: Int,
) {
    item("${prefix}_header", contentType = "header") {
        CardDetailsSectionHeader(
            title = stringResource(sectionTitleResId),
            normalCase = false
        )
    }
}

fun LazyListScope.cardDetailsWithLinkedBack(
    relatedCard: RelatedCard,
    prefix: String,
    collection: Collection
) {
    relatedCard.details.run {
        doubleSidedCardDetails(
            cardDetailsWithPackInfo = this,
            prefix = prefix,
            collection = collection
        )
    }

    relatedCard.backDetails?.run {
        doubleSidedCardDetails(
            cardDetailsWithPackInfo = this,
            prefix = prefix,
            collection = collection,
            suffix = "_back"
        )
    }
}

private fun RelatedCard.shouldShow(
    collection: Collection,
    ignoreCollection: Boolean,
    showFanmade: Boolean
): Boolean {
    if (!showFanmade && !details.cardDetails.official) return false

    return if (ignoreCollection) true else details.firstPackIn(collection) != null
}