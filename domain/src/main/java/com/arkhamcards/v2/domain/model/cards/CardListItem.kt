package com.arkhamcards.v2.domain.model.cards

import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction

data class CardListItem(
    val id: String,
    val thumbnailUrl: String?,

    //Cost
    val cost: String?,
    val xp: Int?,
    val permanent: Boolean,

    //Taboo
    val tabooXp: Int?,
    val tabooSetId: Int?,

    //Type
    val type: CardType,
    val typeName: String,
    val typeNumber: Int,
    val subType: CardSubType?,
    val subTypeName: String?,

    //Faction
    val faction: Faction,
    val faction2: Faction?,
    val faction3: Faction?,
    val factionNumber: Int,

    //Slot
    val slot: String?,
    val slotNumber: Int,

    //Pack
    val packCode: String,
    val packPosition: Int,
    val packName: String,
    val cycleCode: String,
    val cycleName: String,
    val cyclePosition: Int,
    val encounterCode: String?,
    val encounterName: String?,
    val reprintPackCode: String?,

    //Name
    val name: String,
    val subname: String?,

    //Skill
    val skillWillpower: Int?,
    val skillIntellect: Int?,
    val skillCombat: Int?,
    val skillAgility: Int?,
    val skillWild: Int?,

    val parallel: Boolean,
    val isUnique: Boolean,
    val stage: Int?,
)
