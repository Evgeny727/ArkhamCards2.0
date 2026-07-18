package com.arkhamcards.v2.domain.enums

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
}