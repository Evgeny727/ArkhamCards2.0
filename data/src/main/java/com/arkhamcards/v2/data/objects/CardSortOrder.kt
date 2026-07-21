package com.arkhamcards.v2.data.objects

object CardSortOrder {

    //first general types and in the end - weaknesses
    fun sortByTypeOrder(type: String, subtype: String?): Int {
        return when (subtype) {
            "basicweakness" -> 14
            "weakness" -> 15

            else -> {
                when (type) {
                    "investigator" -> 0
                    "asset" -> 1
                    "event" -> 2
                    "skill" -> 3
                    "location" -> 4
                    "enemy" -> 5
                    "enemy_location" -> 6
                    "key" -> 7
                    "treachery" -> 8
                    "scenario" -> 9
                    "act" -> 10
                    "agenda" -> 11
                    "story" -> 12
                    else -> 13
                }
            }
        }
    }

    //"guardian","seeker","rogue","mystic","survivor","neutral","multiclass","mythos"
    fun sortByFactionOrder(faction: String, faction2: String?): Int {
        val basicFactions = listOf("guardian", "seeker", "rogue", "mystic", "survivor", "neutral")
        if (faction2 != null) return 6
        return when (faction) {
            in basicFactions -> {
                basicFactions.indexOf(faction)
            }
            else -> 7
        }
    }

    fun sortBySlotOrder(slot: String?, isPermanent: Boolean?, type: String): Int {
        if (isPermanent == true) return  99
        return when(slot) {
            null -> if (type == "asset") 100 else 0
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