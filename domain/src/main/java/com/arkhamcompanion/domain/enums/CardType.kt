package com.arkhamcompanion.domain.enums

enum class CardType(val code: String) {
    Act("act"),
    Agenda("agenda"),
    Asset("asset"),
    Enemy("enemy"),
    EnemyLocation("enemy_location"),
    Event("event"),
    Investigator("investigator"),
    Key("key"),
    Location("location"),
    Scenario("scenario"),
    Skill("skill"),
    Story("story"),
    Treachery("treachery"),
    Unknown("unknown");

    companion object {
        fun byType(type: String) = CardType.entries.find { it.code == type } ?: Unknown
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