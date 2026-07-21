package com.arkhamcards.v2.domain.model.cards

sealed interface CardListItemUiModel {
    data class CardItem(val card: CardListItem) : CardListItemUiModel
    data class CategoryHeader(val key: String, val category: CardsHeaderType?, val value: Any?) : CardListItemUiModel
}

enum class CardsHeaderType {
    TYPE, TYPE_SLOT, SLOT, FACTION, FACTION_PACK, FACTION_LEVEL, COST, LEVEL, CYCLE, PACK,
    PACK_ENCOUNTER_SET, POSITION, ENCOUNTER_SET
}