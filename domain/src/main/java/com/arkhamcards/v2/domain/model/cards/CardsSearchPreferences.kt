package com.arkhamcards.v2.domain.model.cards

import com.arkhamcards.v2.domain.model.settings.Collection
import kotlinx.collections.immutable.persistentListOf

data class CardsSearchPreferences(
    val includeEnglish: Boolean = false,
    val showFanMade: Boolean = false,
    val tabooSetId: Int = 0,
    val playerSortOrder: List<String> = emptyList(),
    val mythosSortOrder: List<String> = emptyList(),
    val ignoreCollection: Boolean = true,
    val collection: Collection = Collection(persistentListOf(), persistentListOf())
)
