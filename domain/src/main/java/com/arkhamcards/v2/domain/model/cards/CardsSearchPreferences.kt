package com.arkhamcards.v2.domain.model.cards

import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.domain.repository.DEFAULT_MYTHOS_SORT_ORDER
import com.arkhamcards.v2.domain.repository.DEFAULT_PLAYER_SORT_ORDER
import kotlinx.collections.immutable.persistentListOf

data class CardsSearchPreferences(
    val includeEnglish: Boolean = false,
    val showFanMade: Boolean = false,
    val tabooSetId: Int = 0,
    val playerSortOrder: List<String> = DEFAULT_PLAYER_SORT_ORDER,
    val mythosSortOrder: List<String> = DEFAULT_MYTHOS_SORT_ORDER,
    val ignoreCollection: Boolean = true,
    val collection: Collection = Collection(persistentListOf(), persistentListOf())
)
