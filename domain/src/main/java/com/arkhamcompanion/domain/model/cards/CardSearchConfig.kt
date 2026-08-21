package com.arkhamcompanion.domain.model.cards

data class CardSearchConfig(
    val spoiler: Boolean,
    val options: CardSearchOptions,
    val preferences: CardSearchPreferences,
    val filters: CardFilters
)
