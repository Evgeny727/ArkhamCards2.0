package com.arkhamcompanion.domain.model.cards

data class CardSearchOptions(
    val searchQuery: String = "",
    val searchGame: Boolean = false,
    val searchFlavor: Boolean = false,
    val searchBack: Boolean = false,
)
