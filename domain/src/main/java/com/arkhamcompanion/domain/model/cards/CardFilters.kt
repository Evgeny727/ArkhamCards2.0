package com.arkhamcompanion.domain.model.cards

import com.arkhamcompanion.domain.enums.CardSubType
import com.arkhamcompanion.domain.enums.CardType
import com.arkhamcompanion.domain.enums.Faction
import com.arkhamcompanion.domain.model.settings.Collection

data class CardFilters(
    val factions: Set<Faction> = emptySet(),
    val levelFilter: LevelFilter? = null,
    val types: Set<CardType> = emptySet(),
    val subTypes: Set<CardSubType?> = emptySet(),
    val costFilter: CostFilter? = null,
    val skillsFilter: SkillsFilter? = null,
    val actions: Set<String> = emptySet(),
    val traits: Set<String> = emptySet(),
    val healthSanityFilter: HealthSanityFilter? = null,
    val assetFilter: AssetFilter? = null,
    val propertiesFilter: PropertiesFilter? = null,
    val enemyFilter: EnemyFilter? = null,
    val locationFilter: LocationFilter? = null,
    val encounterSets: Set<String> = emptySet(),
    val officialFilter: Boolean? = null,
    val packs: Collection? = null,
    val tabooSetId: String? = null,
    val illustrators: Set<String> = emptySet(),
)

data class LevelFilter(
    val range: NullableIntRange = NullableIntRange(null, 5),
)

data class CostFilter(
    val range: NullableIntRange = NullableIntRange(null, 20),
    val xCost: Boolean = false,
    val evenCost: Boolean = false,
    val oddCost: Boolean = false,
)

data class SkillsFilter(
    val willpower: Int? = null,
    val intellect: Int? = null,
    val combat: Int? = null,
    val agility: Int? = null,
    val wild: Int? = null,
    val any: Int? = null,
)

data class AssetFilter(
    val slots: Set<String> = emptySet(),
    val uses: Set<String> = emptySet(),
    val skillBoosts: Set<String> = emptySet(),
)

data class HealthSanityFilter(
    val health: NullableIntRange = NullableIntRange(null, 15),
    val sanity: NullableIntRange = NullableIntRange(null, 9),
    val includeXHealthOrSanity: Boolean = false,
    val healthPerInvestigator: Boolean = false,
)

data class PropertiesFilter(
    val customizable: Boolean = false,
    val exile: Boolean = false,
    val exceptional: Boolean = false,
    val fast: Boolean = false,
    val healsDamage: Boolean = false,
    val healsHorror: Boolean = false,
    val multiclass: Boolean = false,
    val myriad: Boolean = false,
    val permanent: Boolean = false,
    val seal: Boolean = false,
    val specialist: Boolean = false,
    val succeedBy: Boolean = false,
    val unique: Boolean = false,
    val victory: Boolean = false,
)

data class EnemyFilter(
    val fight: NullableIntRange = NullableIntRange(null, 8),
    val evade: NullableIntRange = NullableIntRange(null, 8),
    val damage: NullableIntRange = NullableIntRange(null, 3),
    val horror: NullableIntRange = NullableIntRange(null, 5),
    val vengeance: Boolean = false,
)

data class LocationFilter(
    val shroud: NullableIntRange = NullableIntRange(null, 9),
    val clues: NullableIntRange = NullableIntRange(null, 12),
    val xShroud: Boolean = false,
    val perInvestigatorClues: Boolean = false,
)

data class NullableIntRange(
    val min: Int?,
    val max: Int?,
)
