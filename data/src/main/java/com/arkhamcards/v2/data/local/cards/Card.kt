package com.arkhamcards.v2.data.local.cards

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.JsonElement

data class Skills(
    @ColumnInfo("skill_willpower")
    val skillWillpower: Int?,
    @ColumnInfo("skill_intellect")
    val skillIntellect: Int?,
    @ColumnInfo("skill_combat")
    val skillCombat: Int?,
    @ColumnInfo("skill_agility")
    val skillAgility: Int?,
    @ColumnInfo("skill_wild")
    val skillWild: Int?
)

data class Translation(
    @ColumnInfo("back_flavor")
    val backFlavor: String?,
    @ColumnInfo("back_name")
    val backName: String?,
    @ColumnInfo("back_subname")
    val backSubname: String?,
    @ColumnInfo("back_text")
    val backText: String?,
    @ColumnInfo("back_traits")
    val backTraits: String?,
    @ColumnInfo("customization_change")
    val customizationChange: String?,
    @ColumnInfo("customization_text")
    val customizationText: String?,
    val flavor: String?,
    val name: String?,
    val slot: String?,
    val subname: String?,
    @ColumnInfo("taboo_original_back_text")
    val tabooOriginalBackText: String?,
    @ColumnInfo("taboo_original_text")
    val tabooOriginalText: String?,
    @ColumnInfo("taboo_text_change")
    val tabooTextChange: String?,
    val text: String?,
    val traits: String?
)

@Entity(tableName = "card")
data class CardEntity(
    @PrimaryKey val id: String,
    val code: String,
    @ColumnInfo("advanced_for")
    val advancedFor: String?,
    @ColumnInfo("alt_art_investigator")
    val altArtInvestigator: Boolean?,
    @ColumnInfo("alternate_of_code")
    val alternateOfCode: String?,
    @ColumnInfo("alternate_required_code")
    val alternateRequiredCode: String?,
    @ColumnInfo("back_link_id")
    val backLinkId: String?,
    @ColumnInfo("back_illustrator")
    val backIllustrator: String?,
    val clues: Int?,
    @ColumnInfo("clues_fixed")
    val cluesFixed: Boolean?,
    val cost: Int?,
    @ColumnInfo("customization_options")
    val customizationOptions: JsonElement?,
    @ColumnInfo("deck_limit")
    val deckLimit: Int?,
    @ColumnInfo("deck_options")
    val deckOptions: JsonElement?,
    @ColumnInfo("deck_requirements")
    val deckRequirements: JsonElement?,
    val doom: Int?,
    @ColumnInfo("double_sided")
    val doubleSided: Boolean?,
    @ColumnInfo("duplicate_of_code")
    val duplicateOfCode: String?,
    @ColumnInfo("encounter_code")
    val encounterCode: String?,
    @ColumnInfo("encounter_position")
    val encounterPosition: Int?,
    @ColumnInfo("enemy_damage")
    val enemyDamage: Int?,
    @ColumnInfo("enemy_horror")
    val enemyHorror: Int?,
    @ColumnInfo("enemy_fight")
    val enemyFight: Int?,
    @ColumnInfo("enemy_evade")
    val enemyEvade: Int?,
    @ColumnInfo("errata_date")
    val errataDate: String?,
    val exceptional: Boolean?,
    val exile: Boolean?,
    @ColumnInfo("faction_code")
    val factionCode: String,
    @ColumnInfo("faction2_code")
    val faction2Code: String?,
    @ColumnInfo("faction3_code")
    val faction3Code: String?,
    val gender: String?,
    @ColumnInfo("heals_damage")
    val healsDamage: Boolean?,
    @ColumnInfo("heals_horror")
    val healsHorror: Boolean?,
    val health: Int?,
    @ColumnInfo("health_per_investigator")
    val healthPerInvestigator: Boolean?,
    val hidden: Boolean?,
    val illustrator: String?,
    @ColumnInfo("investigator_id")
    val investigatorId: String?,
    @ColumnInfo("is_unique")
    val isUnique: Boolean?,
    val linked: Boolean?,
    @ColumnInfo("linked_card")
    val linkedCard: String?,
    val myriad: Boolean?,
    val official: Boolean,
    @ColumnInfo("pack_code")
    val packCode: String,
    @ColumnInfo("pack_position")
    val packPosition: Int,
    @ColumnInfo("parallel_of_code")
    val parallelOfCode: String?,
    val permanent: Boolean?,
    val position: Int,
    val preview: Boolean?,
    @ColumnInfo("real_back_flavor")
    val realBackFlavor: String?,
    @ColumnInfo("real_back_name")
    val realBackName: String?,
    @ColumnInfo("real_back_subname")
    val realBackSubname: String?,
    @ColumnInfo("real_back_text")
    val realBackText: String?,
    @ColumnInfo("real_back_traits")
    val realBackTraits: String?,
    @ColumnInfo("real_customization_change")
    val realCustomizationChange: String?,
    @ColumnInfo("real_customization_text")
    val realCustomizationText: String?,
    @ColumnInfo("real_encounter_set_name")
    val realEncounterSetName: String?,
    @ColumnInfo("real_flavor")
    val realFlavor: String?,
    @ColumnInfo("real_name")
    val realName: String,
    @ColumnInfo("real_pack_name")
    val realPackName: String,
    @ColumnInfo("real_slot")
    val realSlot: String?,
    @ColumnInfo("real_subname")
    val realSubname: String?,
    @ColumnInfo("real_taboo_original_back_text")
    val realTabooOriginalBackText: String?,
    @ColumnInfo("real_taboo_original_text")
    val realTabooOriginalText: String?,
    @ColumnInfo("real_taboo_text_change")
    val realTabooTextChange: String?,
    @ColumnInfo("real_text")
    val realText: String?,
    @ColumnInfo("real_traits")
    val realTraits: String?,
    @ColumnInfo("replacement_for")
    val replacementFor: String?,
    @ColumnInfo("reprint_of_code")
    val reprintOfCode: String?,
    val restrictions: JsonElement?,
    val sanity: Int?,
    val shroud: Int?,
    @ColumnInfo("side_deck_options")
    val sideDeckOptions: JsonElement?,
    @ColumnInfo("side_deck_requirements")
    val sideDeckRequirements: JsonElement?,
    @ColumnInfo("signature_for")
    val signatureFor: String?,
    @ColumnInfo("simple_deck_requirements")
    val simpleDeckRequirements: JsonElement?,
    @ColumnInfo("simple_side_deck_requirements")
    val simpleSideDeckRequirements: JsonElement?,
    @Embedded
    val skills: Skills?,
    val spoiler: Boolean?,
    val stage: Int?,
    @ColumnInfo("subtype_code")
    val subTypeCode: String?,
    val tags: JsonElement?,
    val xp: Int?,
    val vengeance: Int?,
    val victory: Int?,
    val quantity: Int,
    @ColumnInfo("type_code")
    val typeCode: String,

    //Image fields
    val imageurl: String?,
    val backimageurl: String?,

    //Taboo fields
    @ColumnInfo("taboo_xp")
    val tabooXp: Int?,
    @ColumnInfo("taboo_placeholder")
    val tabooPlaceholder: Boolean?,
    @ColumnInfo("taboo_set_id")
    val tabooSetId: Int?,


    //Translation
    @Embedded
    val translation: Translation
)
