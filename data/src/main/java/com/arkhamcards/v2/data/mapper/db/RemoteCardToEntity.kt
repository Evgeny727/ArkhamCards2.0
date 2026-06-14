package com.arkhamcards.v2.data.mapper.db

import com.arkhamcards.v2.GetTranslationDataQuery
import com.arkhamcards.v2.data.local.cards.CardEntity
import com.arkhamcards.v2.data.local.cards.CardSubtypeEntity
import com.arkhamcards.v2.data.local.cards.CardTypeEntity
import com.arkhamcards.v2.data.local.cards.Skills
import com.arkhamcards.v2.data.local.cards.Translation
import com.arkhamcards.v2.fragment.CoreCardText
import com.arkhamcards.v2.fragment.SingleCard

/**
 * Extension function to convert [SingleCard] with [CoreCardText] to [CardEntity]
 */
fun SingleCard.toEntity(coreCardText: CoreCardText): CardEntity {
    val translation = coreCardText.toTranslation()
    return CardEntity(
        id = id,
        code = code,
        advancedFor = advanced_for,
        altArtInvestigator = alt_art_investigator,
        alternateOfCode = alternate_of_code,
        alternateRequiredCode = alternate_required_code,
        backLinkId = back_link_id,
        backIllustrator = back_illustrator,
        clues = clues,
        cluesFixed = clues_fixed,
        cost = cost,
        customizationOptions = customization_options,
        deckLimit = deck_limit,
        deckOptions = deck_options,
        deckRequirements = deck_requirements,
        doom = doom,
        doubleSided = double_sided,
        duplicateOfCode = duplicate_of_code,
        encounterCode = encounter_code,
        encounterPosition = encounter_position,
        enemyDamage = enemy_damage,
        enemyHorror = enemy_horror,
        enemyFight = enemy_fight,
        enemyEvade = enemy_evade,
        errataDate = errata_date,
        exceptional = exceptional,
        exile = exile,
        factionCode = faction_code,
        faction2Code = faction2_code,
        faction3Code = faction3_code,
        gender = gender?.rawValue,
        healsDamage = heals_damage,
        healsHorror = heals_horror,
        health = health,
        healthPerInvestigator = health_per_investigator,
        hidden = hidden,
        illustrator = illustrator,
        investigatorId = investigator_id,
        isUnique = is_unique,
        linked = linked,
        linkedCard = linked_card?.code,
        myriad = myriad,
        official = official,
        packCode = pack_code,
        packPosition = pack_position,
        parallelOfCode = parallel_of_code,
        permanent = permanent,
        position = position,
        preview = preview,
        realBackFlavor = real_back_flavor,
        realBackName = real_back_name,
        realBackSubname = real_back_subname,
        realBackText = real_back_text,
        realBackTraits = real_back_traits,
        realCustomizationChange = real_customization_change,
        realCustomizationText = real_customization_text,
        realEncounterSetName = real_encounter_set_name,
        realFlavor = real_flavor,
        realName = real_name,
        realPackName = real_pack_name,
        realSlot = real_slot,
        realSubname = real_subname,
        realTabooOriginalBackText = real_taboo_original_back_text,
        realTabooOriginalText = real_taboo_original_text,
        realTabooTextChange = real_taboo_text_change,
        realText = real_text,
        realTraits = real_traits,
        replacementFor = replacement_for,
        reprintOfCode = reprint_of_code,
        restrictions = restrictions,
        sanity = sanity,
        shroud = shroud,
        sideDeckOptions = side_deck_options,
        sideDeckRequirements = side_deck_requirements,
        signatureFor = signature_for,
        simpleDeckRequirements = simple_deck_requirements,
        simpleSideDeckRequirements = simple_side_deck_requirements,
        skills = Skills(
            skillWillpower = skill_willpower,
            skillIntellect = skill_intellect,
            skillCombat = skill_combat,
            skillAgility = skill_agility,
            skillWild = skill_wild
        ),
        spoiler = spoiler,
        stage = stage,
        subTypeCode = subtype_code,
        tags = tags,
        xp = xp,
        vengeance = vengeance,
        victory = victory,
        quantity = quantity,
        typeCode = type_code.rawValue,
        imageurl = imageurl,
        backimageurl = backimageurl,
        tabooXp = taboo_xp,
        tabooPlaceholder = taboo_placeholder,
        tabooSetId = taboo_set_id,
        translation = translation
    )
}

/**
 * Extension function to convert [CoreCardText] to [Translation]
 */
fun CoreCardText.toTranslation(): Translation = Translation(
    backFlavor = back_flavor,
    backName = back_name,
    backSubname = back_subname,
    backText = back_text,
    backTraits = back_traits,
    customizationChange = customization_change,
    customizationText = customization_text,
    flavor = flavor,
    name = name,
    slot = slot,
    subname = subname,
    tabooOriginalBackText = taboo_original_back_text,
    tabooOriginalText = taboo_original_text,
    tabooTextChange = taboo_text_change,
    text = text,
    traits = traits
)

/**
 * Extension function to convert [GetTranslationDataQuery.Card_type_name] to [CardTypeEntity]
 */
fun GetTranslationDataQuery.Card_type_name.toEntity(): CardTypeEntity {
    return CardTypeEntity(
        code = code.rawValue,
        name = name
    )
}

/**
 * Extension function to convert [GetTranslationDataQuery.Card_subtype_name] to [CardSubtypeEntity]
 */
fun GetTranslationDataQuery.Card_subtype_name.toEntity(): CardSubtypeEntity {
    return CardSubtypeEntity(
        code = code,
        name = name
    )
}