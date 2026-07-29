package com.arkhamcards.v2.domain.enums

enum class CardBackType {
    Player, Encounter, Card, TheLongestNight, Artifact, CthulhuDeck;

    companion object {
        fun byType(type: String) = when (type) {
            "player" -> Player
            "encounter" -> Encounter
            "card" -> Card
            "the_longest_night" -> TheLongestNight
            "artifact" -> Artifact
            "cthulhu_deck" -> CthulhuDeck
            else -> Player
        }
    }
}