package com.arkhamcards.v2.data.local.cards

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CardListItemEntity(
    val id: String,
    val thumbnailurl: String?,

    //Cost
    val cost: Int?,
    val xp: Int?,
    val permanent: Boolean,

    //Taboo
    @ColumnInfo(name = "taboo_xp")
    val tabooXp: Int?,
    @ColumnInfo(name = "taboo_set_id")
    val tabooSetId: Int?,

    //Type
    @ColumnInfo(name = "type_code")
    val typeCode: String,
    val typeName: String,
    @ColumnInfo(name = "subtype_code")
    val subTypeCode: String?,
    val subTypeName: String?,

    //Faction
    @ColumnInfo(name = "faction_code")
    val factionCode: String,
    val factionName: String,
    @ColumnInfo(name = "faction2_code")
    val faction2Code: String?,
    @ColumnInfo(name = "faction3_code")
    val faction3Code: String?,

    //Pack + Encounter + Cycle
    @ColumnInfo(name = "pack_code")
    val packCode: String,
    val packName: String,
    @ColumnInfo(name = "pack_position")
    val packPosition: Int,
    @ColumnInfo(name = "encounter_code")
    val encounterCode: String?,
    val encounterName: String?,
    @ColumnInfo(name = "cycle_code")
    val cycleCode: String,
    val cycleName: String,

    //Name
    val name: String,
    val subname: String?,

    //Skill
    @Embedded
    val skills: Skills,

    val parallel: Boolean,
    @ColumnInfo(name = "is_unique")
    val isUnique: Boolean,
    val slot: String?,
    val stage: Int?,
)