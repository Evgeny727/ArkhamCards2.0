package com.arkhamcards.v2.domain.enums

enum class Faction {
    Guardian, Seeker, Rogue, Mystic, Survivor, Neutral, Mythos, Dual, Dead;

    companion object {
        fun byFaction(faction: String) = when (faction) {
            "guardian" -> Guardian
            "seeker" -> Seeker
            "rogue" -> Rogue
            "mystic" -> Mystic
            "survivor" -> Survivor
            "neutral" -> Neutral
            "mythos" -> Mythos
            "dual" -> Dual
            "dead" -> Dead
            else -> throw IllegalArgumentException("Invalid faction: $faction")
        }
    }
}