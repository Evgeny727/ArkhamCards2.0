package com.arkhamcards.v2.domain.model.cards

data class CardsSearchOptions(
    val searchQuery: String = "",
    val searchGame: Boolean = false,
    val searchFlavor: Boolean = false,
    val searchBack: Boolean = false,
)
