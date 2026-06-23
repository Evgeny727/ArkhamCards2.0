package com.arkhamcards.v2.data.objects

import com.arkhamcards.v2.fragment.SingleCard
import com.arkhamcards.v2.type.Card_type_code_enum

object CardSortOrder {

    //"investigator","asset","event","skill","basicweakness","signature_weakness","weakness","scenario","story"
    fun sortByTypeOrder(card: SingleCard): Int {
        if (card.spoiler == true) return 8
        return when (card.subtype_code) {
            "basicweakness" -> 4
            "weakness" -> {
                if (card.restrictions?.toString()?.contains("investigator") == true) 5
                else 6
            }

            else -> {
                when (card.type_code) {
                    Card_type_code_enum.investigator -> 0
                    Card_type_code_enum.asset -> 1
                    Card_type_code_enum.event -> 2
                    Card_type_code_enum.skill -> 3
                    else -> 7
                }
            }
        }
    }

    //"guardian","seeker","rogue","mystic","survivor","neutral","specialist","multiclass","mythos"
    fun sortByFactionOrder(card: SingleCard): Int {
        val basicFactions = listOf("guardian", "seeker", "rogue", "mystic", "survivor", "neutral")
        if (card.faction2_code != null) return 7
        if (card.restrictions?.toString()?.contains("trait") == true) return 6
        return when (card.faction_code) {
            in basicFactions -> {
                basicFactions.indexOf(card.faction_code)
            }
            else -> 8
        }
    }

    fun sortBySlotOrder(slot: String?, isPermanent: Boolean?, type: String): Int {
        if (isPermanent == true) return  99
        return when(slot) {
            null -> if (type == "asset") 0 else 100
            "Hand" -> 1
            "Hand. Arcane" -> 2
            "Hand x2" -> 3
            "Hand x2. Arcane" -> 4
            "Accessory" -> 5
            "Ally" -> 6
            "Ally. Arcane" -> 7
            "Arcane" -> 8
            "Arcane x2" -> 9
            "Arcane. Accessory" -> 10
            "Head" -> 11
            "Body" -> 12
            "Body. Arcane" -> 13
            "Body. Hand x2" -> 14
            "Tarot" -> 15
            else -> 16
        }
    }
}