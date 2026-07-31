package com.arkhamcards.v2.domain.model.cards

import kotlinx.collections.immutable.ImmutableList

data class CardDetailsWithRelations(
    val cardDetails: CardDetailsWithPackInfo,
    val backCardDetails: CardDetailsWithPackInfo?,
    val cardRelations: CardRelations,
)

data class CardRelations(

    val bound: ImmutableList<CardDetailsWithPackInfo>,
    val bonded: ImmutableList<CardDetailsWithPackInfo>,

    val restrictedTo: ImmutableList<CardDetailsWithPackInfo>,

    val parallel: CardDetailsWithPackInfo?,
    val base: CardDetailsWithPackInfo?,

    val advanced: ImmutableList<CardDetailsWithPackInfo>,
    val replacement: ImmutableList<CardDetailsWithPackInfo>,
    val requiredCards: ImmutableList<CardDetailsWithPackInfo>,
    val sideDeckRequiredCards: ImmutableList<CardDetailsWithPackInfo>,
    val parallelCards: ImmutableList<CardDetailsWithPackInfo>,
    val otherVersions: ImmutableList<CardDetailsWithPackInfo>,
    val level: ImmutableList<CardDetailsWithPackInfo>,
    val otherSignatures: ImmutableList<CardDetailsWithPackInfo>,
)

data class CardDetailsWithPackInfo(
    val cardDetails: CardDetails,
    val duplicates: ImmutableList<CardPackInfo>,
    val reprints: ImmutableList<CardPackInfo>,
)