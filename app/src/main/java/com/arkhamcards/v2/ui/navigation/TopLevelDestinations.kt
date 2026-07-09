package com.arkhamcards.v2.ui.navigation

import androidx.annotation.StringRes
import com.arkhamcards.v2.R
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.IconGlyph
import kotlinx.serialization.Serializable

sealed interface BottomBarItem {

    val icon: IconGlyph
    @get:StringRes
    val label: Int

    @Serializable
    data object Cards : BottomBarItem {
        override val icon = AppIcon.Cards
        override val label = R.string.cards
    }

    @Serializable
    data object Decks : BottomBarItem {
        override val icon = AppIcon.Deck
        override val label = R.string.decks
    }

    @Serializable
    data object Campaigns : BottomBarItem {
        override val icon = AppIcon.Book
        override val label = R.string.campaigns
    }

    @Serializable
    data object Settings : BottomBarItem {
        override val icon = AppIcon.Settings
        override val label = R.string.settings
    }
}