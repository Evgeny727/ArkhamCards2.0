package com.arkhamcompanion.ui.navigation

import androidx.annotation.StringRes
import com.arkhamcompanion.R
import com.arkhamcompanion.ui.icons.AppIcon
import kotlinx.serialization.Serializable

sealed interface BottomBarItem {

    val icon: com.arkhamcompanion.ui.icons.IconGlyph
    @get:StringRes
    val label: Int
    val startDestination: Any

    @Serializable
    data object Cards : BottomBarItem {
        override val icon = AppIcon.Cards
        override val label = R.string.cards
        override val startDestination = com.arkhamcompanion.ui.cards.Cards
    }

    @Serializable
    data object Decks : BottomBarItem {
        override val icon = AppIcon.Deck
        override val label = R.string.decks
        override val startDestination = com.arkhamcompanion.ui.decks.Decks
    }

    @Serializable
    data object Campaigns : BottomBarItem {
        override val icon = AppIcon.Book
        override val label = R.string.campaigns
        override val startDestination = com.arkhamcompanion.ui.campaigns.Campaigns
    }

    @Serializable
    data object Settings : BottomBarItem {
        override val icon = AppIcon.Settings
        override val label = R.string.settings
        override val startDestination = com.arkhamcompanion.ui.settings.Settings
    }
}