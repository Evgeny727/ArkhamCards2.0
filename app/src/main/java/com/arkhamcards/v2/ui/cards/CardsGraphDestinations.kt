package com.arkhamcards.v2.ui.cards

import kotlinx.serialization.Serializable

@Serializable
object Cards

@Serializable
object CardsSortScreen

@Serializable
data class CardDetailsScreen(
    val cardCode: String
)