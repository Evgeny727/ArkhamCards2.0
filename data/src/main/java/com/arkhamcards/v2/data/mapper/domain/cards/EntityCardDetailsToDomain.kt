package com.arkhamcards.v2.data.mapper.domain.cards

import com.arkhamcards.v2.data.local.cards.CardDetailsEntity
import com.arkhamcards.v2.data.objects.CardCache
import com.arkhamcards.v2.data.objects.CardTextParser
import com.arkhamcards.v2.domain.enums.CardBackType
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardDetails
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithPackInfo
import com.arkhamcards.v2.domain.model.cards.CardPackInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun CardDetailsEntity.toDomain() = CardDetails(
    id = id,
    code = code,
    backName = backName,
    backSubname = backSubname,
    backTraits = backTraits,
    name = name,
    slot = slot,
    subname = subname,
    traits = traits,
    backIllustrator = backIllustrator,
    backType = CardBackType.byType(backType),
    clues = clues,
    cluesFixed = cluesFixed,
    cost = cost.realCardCost(typeCode, permanent),
    doom = doom,
    doomPerInvestigator = doomPerInvestigator,
    doubleSided = doubleSided,
    duplicateOfCode = duplicateOfCode,
    deckLimit = deckLimit,
    encounterCode = encounterCode,
    encounterPosition = encounterPosition,
    encounterName = encounterName,
    enemyDamage = enemyDamage,
    enemyHorror = enemyHorror,
    enemyFight = enemyFight,
    enemyFightPerInvestigator = enemyFightPerInvestigator,
    enemyEvade = enemyEvade,
    enemyEvadePerInvestigator = enemyEvadePerInvestigator,
    faction = Faction.byFaction(factionCode),
    faction2 = faction2Code?.let { Faction.byFaction(faction2Code) },
    faction3 = faction3Code?.let { Faction.byFaction(faction3Code) },
    health = health,
    healthPerInvestigator = healthPerInvestigator,
    illustrator = illustrator,
    isUnique = isUnique,
    official = official,
    packCode = packCode,
    packName = packName,
    packPosition = packPosition,
    parallel = parallel,
    permanent = permanent,
    reprintPackCode = reprintPackCode,
    reprintPackName = reprintPackName,
    realSlot = realSlot,
    sanity = sanity,
    shroud = shroud,
    shroudPerInvestigator = shroudPerInvestigator,
    skillWillpower = skillWillpower,
    skillIntellect = skillIntellect,
    skillCombat = skillCombat,
    skillAgility = skillAgility,
    skillWild = skillWild,
    stage = stage,
    subType = subTypeCode?.let { CardSubType.bySubType(subTypeCode) },
    subTypeName = subTypeName,
    xp = xp,
    vengeance = vengeance,
    victory = victory,
    quantity = quantity,
    type = CardType.byType(typeCode),
    typeName = typeName,
    thumbnailUrl = thumbnailurl,
    imageUrl = imageurl,
    backImageUrl = backimageurl,
    tabooSetId = tabooSetId,
    tabooXp = tabooXp,
    tabooPlaceholder = tabooPlaceholder,
    parsedBackFlavor = backFlavor?.let { CardTextParser.parse(it) },
    parsedBackText = backText?.let { CardTextParser.parse(it) },
    parsedFlavor = flavor?.let { CardTextParser.parse(it) },
    parsedText = text?.let { CardTextParser.parse(it) },
    parsedCustomizationText = customizationText?.let { CardTextParser.parse(it) },
    parsedTabooOriginalBackText = tabooOriginalBackText?.let { CardTextParser.parse(it) },
    parsedTabooOriginalText = tabooOriginalText?.let { CardTextParser.parse(it) },
)

internal fun List<CardDetailsEntity>.toDetailsWithPackInfo(): Map<String, CardDetailsWithPackInfo> {
    val map = HashMap<String, CardDetailsEntity>(size)
    forEach {
        map[it.code] = it
    }

    val detailsWithPackInfoMap = HashMap<String, CardDetailsWithPackInfo>(size)
    for ((code, entity) in map) {

        val reprints =
            buildPackInfoList(
                CardCache.reprints[code],
                map,
            )

        val duplicates = buildSet {
            addAll(buildPackInfoList(
                CardCache.duplicates[code],
                map,
            ))
            CardCache.reprints[code]?.forEach { code ->
                addAll(buildPackInfoList(
                    CardCache.duplicates[code],
                    map,
                ))
            }
        }.toImmutableList()

        detailsWithPackInfoMap[code] =
            CardDetailsWithPackInfo(
                cardDetails = entity.toDomain(),
                duplicates = duplicates,
                reprints = reprints,
            )
    }

    return detailsWithPackInfoMap
}

private fun buildPackInfoList(
    codes: Set<String>?,
    details: Map<String, CardDetailsEntity>,
): ImmutableList<CardPackInfo> {
    return buildList {
        codes?.forEach { code ->
            details[code]?.let { add(it.toPackInfoDomain()) }
        }
    }.toImmutableList()
}

internal fun CardDetailsEntity.toPackInfoDomain() = CardPackInfo(
    code = packCode,
    reprintCode = reprintPackCode,
    name = packName,
    reprintName = reprintPackName,
    quantity = quantity,
    position = packPosition
)