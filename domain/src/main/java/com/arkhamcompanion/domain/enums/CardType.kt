package com.arkhamcompanion.domain.enums

enum class CardType {
    Act, Agenda, Asset, Enemy, EnemyLocation, Event, Investigator, Key, Location, Scenario, Skill, Story, Treachery, Unknown;

    companion object {
        fun byType(type: String) = when (type) {
            "act" -> Act
            "agenda" -> Agenda
            "asset" -> Asset
            "enemy" -> Enemy
            "enemy_location" -> EnemyLocation
            "event" -> Event
            "investigator" -> Investigator
            "key" -> Key
            "location" -> Location
            "scenario" -> Scenario
            "skill" -> Skill
            "story" -> Story
            "treachery" -> Treachery
            else -> Unknown
        }
    }

    fun isLocationLike(): Boolean {
        return this == Location || this == EnemyLocation
    }

    fun isEnemyLike(): Boolean {
        return this == Enemy || this == EnemyLocation
    }

    val imageOffset: Pair<Float, Float>
        get() = when (this) {
            Investigator, Agenda -> -1f to 0f

            Act -> 1f to 0f

            Enemy -> 0f to 1f

            else -> 0f to -1f
        }
}