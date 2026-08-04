package com.arkhamcards.v2.domain.model.cards

import kotlinx.collections.immutable.ImmutableList

data class CardDetailsWithRelations(
    val cardDetails: CardDetailsWithPackInfo,
    val backCardDetails: CardDetailsWithPackInfo?,
    val cardRelations: CardRelations,
)

data class CardRelations(

    val bound: ImmutableList<RelatedCard>,
    val bonded: ImmutableList<RelatedCard>,

    val restrictedTo: ImmutableList<RelatedCard>,

    val parallel: RelatedCard?,
    val base: RelatedCard?,

    val advanced: ImmutableList<RelatedCard>,
    val replacement: ImmutableList<RelatedCard>,
    val requiredCards: ImmutableList<RelatedCard>,
    val sideDeckRequiredCards: ImmutableList<RelatedCard>,
    val parallelCards: ImmutableList<RelatedCard>,
    val otherVersions: ImmutableList<RelatedCard>,
    val level: ImmutableList<RelatedCard>,
    val otherSignatures: ImmutableList<RelatedCard>,
)

data class RelatedCard(
    val details: CardDetailsWithPackInfo,
    val backDetails: CardDetailsWithPackInfo? = null,
)

data class CardDetailsWithPackInfo(
    val cardDetails: CardDetails,
    val duplicates: ImmutableList<CardPackInfo>,
    val reprints: ImmutableList<CardPackInfo>,
)