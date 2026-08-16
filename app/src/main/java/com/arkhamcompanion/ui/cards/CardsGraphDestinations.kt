package com.arkhamcompanion.ui.cards

import kotlinx.serialization.Serializable

@Serializable
object Cards

@Serializable
object CardsSortScreen

@Serializable
object CardsFiltersScreen

@Serializable
data class CardDetailsScreen(
    val cardCode: String
)