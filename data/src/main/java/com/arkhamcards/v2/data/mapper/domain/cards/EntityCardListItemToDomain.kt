package com.arkhamcards.v2.data.mapper.domain.cards

import com.arkhamcards.v2.data.local.cards.CardListItemEntity
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardListItem

fun CardListItemEntity.toDomain() = CardListItem(
    id = id,
    thumbnailUrl = thumbnailurl,
    cost = cost.realCardCost(typeCode, permanent),
    xp = xp,
    permanent = permanent,
    tabooXp = tabooXp,
    tabooSetId = tabooSetId,
    type = CardType.byType(typeCode),
    typeName = typeName,
    subType = subTypeCode?.let { CardSubType.bySubType(subTypeCode) },
    faction = Faction.byFaction(factionCode),
    faction2 = faction2Code?.let { Faction.byFaction(faction2Code) },
    faction3 = faction3Code?.let { Faction.byFaction(faction3Code) },
    packCode = packCode,
    packPosition = packPosition,
    encounterCode = encounterCode,
    reprintPackCode = reprintPackCode,
    name = name,
    subname = subname,
    skillWillpower = skills.skillWillpower,
    skillIntellect = skills.skillIntellect,
    skillCombat = skills.skillCombat,
    skillAgility = skills.skillAgility,
    skillWild = skills.skillWild,
    parallel = parallel,
    isUnique = isUnique,
    stage = stage
)

internal fun Int?.realCardCost(typeCode: String?, permanent: Boolean): String? {
    if (typeCode != "asset" && typeCode != "event") return null
    if (this == -2) return "X"
    if (permanent || this == null) return "-"
    return this.toString()
}