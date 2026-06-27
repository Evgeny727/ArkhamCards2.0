package com.arkhamcards.v2.data.objects

import com.arkhamcards.v2.data.local.cards.CardCacheData
import com.arkhamcards.v2.data.local.cards.CardEntity
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val REGEX_SKILL_BOOST = Regex("""\+\d+?\s\[(.+?)\]""")
private val USES_REGEX = Regex("""Uses\s\(\d+?\s(\w+?)\)""")
private val BONDED_REGEX = Regex("""Bonded\s\((.*?)\)(\.|\s)""")
private val REGEX_SUCCEED_BY = Regex("""succe(ssful|ed(?:s?|ed?))(:? at a skill test)? by(?! 0)""")
private val TAG_REGEX_FALLBACKS: Map<String, Regex> = mapOf(
    "fa" to Regex("""[Ff]irearm"""),
    "hd" to Regex("""[Hh]eal(?!ed)(?!th)(?! in excess of)[^.!?]*?damage"""),
    "hh" to Regex("""[Hh]eal(?!ed)(?!th)(?! in excess of)[^.!?]*?horror"""),
    "pa" to Regex("""[Pp]arley"""),
    "se" to Regex("""[Ss]eal(?! of the)"""),
)
private val ACTION_REGEX =
    Regex("<b>(Fight|Engage|Investigate|Draw|Move|Evade|Parley|Resign)")
private val CHAOS_REGEX =
    Regex("""\[(auto_fail|bless|skull|cultist|curse|tablet|frost|elder_sign|elder_thing)""")

object CardCache {

    var traits: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var actions: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var properties: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var skillBoosts: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var uses: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var chaosTokens: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var tags: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var requiredCards: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var sideDeckRequiredCards: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var restrictedTo: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var advanced: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var replacement: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var parallelCards: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var parallel: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var base: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var duplicates: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var reprints: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var level: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var bound: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var bonded: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var fronts: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var otherVersions: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set
    var basePrints: MutableMap<String, MutableSet<String>> = mutableMapOf()
        private set

    private fun clearCache() {
        traits = mutableMapOf()
        actions = mutableMapOf()
        properties = mutableMapOf()
        skillBoosts = mutableMapOf()
        uses = mutableMapOf()
        chaosTokens = mutableMapOf()
        tags = mutableMapOf()
        requiredCards = mutableMapOf()
        sideDeckRequiredCards = mutableMapOf()
        restrictedTo = mutableMapOf()
        advanced = mutableMapOf()
        replacement = mutableMapOf()
        parallelCards = mutableMapOf()
        parallel = mutableMapOf()
        base = mutableMapOf()
        duplicates = mutableMapOf()
        reprints = mutableMapOf()
        level = mutableMapOf()
        bound = mutableMapOf()
        bonded = mutableMapOf()
        fronts = mutableMapOf()
        otherVersions = mutableMapOf()
        basePrints = mutableMapOf()
    }

    fun createCache(cardsList: List<CardEntity>) {
        clearCache()

        val cardsMap = cardsList.associateBy { it.id }

        val localBonded: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val upgrades: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val backs: MutableMap<String, String> = mutableMapOf()
        val investigatorsByName: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val canonicalInvestigatorCodes: MutableSet<String> = mutableSetOf()
        val requiredCardCodes: MutableSet<String> = mutableSetOf()

        // first pass: identify target cards.
        for (card in cardsList) {
            addIndices(card)

            if ((card.xp ?: -1) >= 0) {
                upgrades.addToSet(card.realName, card.code)
            }

            val bondedMatch = BONDED_REGEX.find(card.realText.orEmpty())
            if (bondedMatch != null) {
                val bondedName = bondedMatch.groupValues.getOrNull(1)
                if (!bondedName.isNullOrEmpty()) {
                    localBonded.addToSet(bondedName, card.code)
                }
            }

            card.backLinkId?.let { backLinkId ->
                backs[backLinkId] = card.code
            }

            if (
                card.typeCode == "investigator" &&
                card.deckLimit != null &&
                card.duplicateOfCode == null &&
                !(card.altArtInvestigator ?: false) &&
                card.alternateOfCode == null &&
                (card.encounterCode.isNullOrEmpty() || card.xp != null)
            ) {
                investigatorsByName.addToSet(card.realName, card.code)
                canonicalInvestigatorCodes.add(card.code)
            }

            card.deckRequirements?.jsonObject["card"]?.jsonArray?.forEach {
                it.jsonArray.forEach { code ->
                    requiredCardCodes.add(code.jsonPrimitive.content)
                }
            }

            card.sideDeckRequirements?.jsonObject["card"]?.jsonArray?.forEach {
                it.jsonArray.forEach { code ->
                    requiredCardCodes.add(code.jsonPrimitive.content)
                }
            }
        }

        // second pass: construct lookup tables.
        for (card in cardsList) {
            card.deckRequirements?.jsonObject["card"]?.jsonArray?.forEach {
                it.jsonArray.forEach { code ->
                    requiredCards.addToSet(card.code, code.jsonPrimitive.content)
                }
            }

            card.sideDeckRequirements?.jsonObject["card"]?.jsonArray?.forEach {
                it.jsonArray.forEach { code ->
                    sideDeckRequiredCards.addToSet(card.code, code.jsonPrimitive.content)
                }
            }

            val investigatorRestrictions = card.restrictions?.jsonObject["investigator"]?.jsonObject
            if (investigatorRestrictions != null && !(card.hidden ?: false)) {
                // Can have multiple entries (alternate arts).
                for (key in investigatorRestrictions.keys) {
                    val investigator = cardsMap[key]

                    if (investigator == null) {
                        //TODO:log missing investigator to crashlytics
                        continue
                    }

                    if (investigator.duplicateOfCode != null) {
                        continue
                    }

                    restrictedTo.addToSet(card.code, key)

                    when {
                        card.realText?.contains("Advanced.") == true ->
                            advanced.addToSet(key, card.code)

                        card.realText?.contains("Replacement.") == true ->
                            replacement.addToSet(key, card.code)

                        else -> {
                            if (card.parallel && !requiredCardCodes.contains(card.code)) {
                                parallelCards.addToSet(key, card.code)
                            } else if (
                                !requiredCardCodes.contains(card.code) &&
                                card.duplicateOfCode == null &&
                                // Kate has bonded cards restricted to her, these should not be part of the deck.
                                card.deckLimit != null
                            ) {
                                requiredCards.addToSet(key, card.code)
                            }
                        }
                    }
                }
            }

            if (card.parallelOfCode != null) {
                parallel.addToSet(card.parallelOfCode, card.code)

                base.addToSet(card.code, card.parallelOfCode)
            }

            card.duplicateOfCode?.let { duplicateOfCode ->
                duplicates.addToSet(duplicateOfCode, card.code)
                duplicates.addToSet(card.code, duplicateOfCode)

                val existingDuplicates = duplicates[duplicateOfCode].orEmpty()
                for (key in existingDuplicates) {
                    if (key != card.code) {
                        duplicates.addToSet(key, card.code)
                        duplicates.addToSet(card.code, key)
                    }
                }
            }

            card.reprintOfCode?.let { reprintOf ->
                reprints.addToSet(reprintOf, card.code)
                reprints.addToSet(card.code, reprintOf)
            }

            if (card.xp != null) {
                val cardUpgrades = upgrades[card.realName]
                if (cardUpgrades != null) {
                    for (upgrade in cardUpgrades) {
                        if (card.code != upgrade) {
                            level.addToSet(card.code, upgrade)
                            level.addToSet(upgrade, card.code)
                        }
                    }
                }
            }

            // TODO: there is an edge case with Dream-Gate where the front should show when accessing `06015b` via
            //  a bond, but currently does not.
            if (!(card.hidden ?: false)) {
                val bondedCards = localBonded[card.realName]
                if (bondedCards != null) {
                    for (bondedCode in bondedCards) {
                        // beware the great hank samson.
                        if (bondedCode != card.code && !card.realText.orEmpty().startsWith("Bonded")) {
                            bound.addToSet(card.code, bondedCode)
                            bonded.addToSet(bondedCode, card.code)
                        }
                    }
                }
            }

            // Index multi-class investigators.
            if (
                card.typeCode == "investigator" &&
                card.deckLimit != null &&
                (investigatorsByName[card.realName]?.size ?: 0) > 1
            ) {
                for (investigator in investigatorsByName[card.realName].orEmpty()) {
                    if (investigator != card.code) {
                        otherVersions.addToSet(card.code, investigator)
                    }
                }
            }
        }

        for ((back, front) in backs) {
            fronts.addToSet(back, front)
        }

        for ((investigator, entry) in parallel) {
            val parallelEntry = entry.firstOrNull() ?: continue

            advanced[parallelEntry] = advanced[investigator] ?: mutableSetOf()
            replacement[parallelEntry] = replacement[investigator] ?: mutableSetOf()
            bonded[parallelEntry] = bonded[investigator] ?: mutableSetOf()
            parallelCards[parallelEntry] = parallelCards[investigator] ?: mutableSetOf()

            for ((key, value) in restrictedTo) {
                if (value.contains(investigator)) {
                    restrictedTo.addToSet(key, parallelEntry)
                }
            }
        }

        for (code in canonicalInvestigatorCodes) {
            val duplicates = duplicates[code] ?: continue

            for (duplicateCode in duplicates) {
                requiredCards[duplicateCode] = requiredCards[code] ?: mutableSetOf()
                advanced[duplicateCode] = advanced[code] ?: mutableSetOf()
                replacement[duplicateCode] = replacement[code] ?: mutableSetOf()
                bonded[duplicateCode] = bonded[code] ?: mutableSetOf()
                parallelCards[duplicateCode] = parallelCards[code] ?: mutableSetOf()
                parallel[duplicateCode] = parallel[code] ?: mutableSetOf()
            }
        }

        for (code in reprints.keys.toList()) {
            val duplicates = duplicates[code] ?: continue

            for (duplicateCode in duplicates) {
                reprints[duplicateCode] = reprints[code] ?: mutableSetOf()

                for (reprintCode in reprints[code].orEmpty()) {
                    if (reprintCode != duplicateCode) {
                        basePrints.addToSet(reprintCode, duplicateCode)
                    }
                }
            }
        }
    }

    private fun addIndices(card: CardEntity) {
        val text = card.realText.orEmpty()
        val backText = card.realBackText.orEmpty()
        val combinedText = text + backText

        indexByTraits(card)
        indexByActions(card, combinedText)
        indexByFast(card, text)
        indexByChaosTokens(card,combinedText)
        indexByTags(card, text)

        // handle additional index based on whether we are dealing with a player card or not.
        if (card.factionCode != "mythos") {
            indexBySucceedsBy(card, text)

            if (card.typeCode == "asset") {
                indexBySkillBoosts(card, text)

                indexByUses(card, text)
            }
        }
    }

    private fun MutableMap<String, MutableSet<String>>.addToSet(
        key: String,
        value: String
    ) {
        getOrPut(key) { mutableSetOf() }.add(value)
    }

    private fun indexByTraits(card: CardEntity) {
        val cardTraits = (card.realTraits ?: "") + (card.realBackTraits ?: "")
        if (cardTraits.isBlank()) return
        for (trait in cardTraits.split(".")) {
            traits.addToSet(trait, card.id)
        }
    }

    private fun indexByActions(card: CardEntity, cardText: String) {
        if (cardText.isBlank()) return
        ACTION_REGEX.findAll(cardText).forEach { match ->
            actions.addToSet(match.groupValues[1], card.id)
        }
    }

    private fun indexByFast(card: CardEntity, cardText: String) {
        if (cardText.contains("Fast.") || cardText.contains("gains fast.")) {
            properties.addToSet("fast", card.id)
        }
    }

    private fun indexBySucceedsBy(card: CardEntity, cardText: String) {
        if (REGEX_SUCCEED_BY.containsMatchIn(cardText)) {
            properties.addToSet("succeeds_by", card.id)
        }
    }

    private fun indexBySkillBoosts(card: CardEntity, cardText: String) {
        if (card.customizationOptions?.toString()?.contains("choose_skill") == true) {
            skillBoosts.addToSet("willpower", card.id)
            skillBoosts.addToSet("intellect", card.id)
            skillBoosts.addToSet("combat", card.id)
            skillBoosts.addToSet("agility", card.id)
        }

        REGEX_SKILL_BOOST.findAll(cardText).forEach { match ->
            val value = match.groupValues.getOrNull(1)
            if (!value.isNullOrEmpty()) {
                skillBoosts.addToSet(match.groupValues[1], card.id)
            }
        }
    }

    private fun indexByUses(card: CardEntity, cardText: String) {
        val firstLine = cardText.indexOf('\n')
        if (firstLine == -1) return
        val usesMatch = USES_REGEX.find(cardText.substring(0, firstLine))?.groupValues?.getOrNull(1)
            ?.lowercase()

        if (usesMatch != null) {
            val value = if (usesMatch == "charge") "charges" else usesMatch
            uses.addToSet(value, card.id)
        }
    }

    private fun indexByChaosTokens(card: CardEntity, cardText: String) {
        if (cardText.isBlank()) return
        CHAOS_REGEX.findAll(cardText).forEach { match ->
            chaosTokens.addToSet(match.groupValues[1], card.id)
        }
    }

    private fun indexByTags(card: CardEntity, cardText: String) {
        if (card.official) {
            if (card.tags !is JsonArray) return
            val parsedTags = card.tags.jsonArray.map { it.jsonPrimitive.content }
            for (tag in parsedTags) {
                tags.addToSet(tag, card.id)
            }
        } else {
            for ((tag, regex) in TAG_REGEX_FALLBACKS) {
                if (regex.containsMatchIn(cardText)) {
                    tags.addToSet(tag, card.id)
                }
            }
        }
    }

    fun load(data: CardCacheData) {
        traits = data.traits.mapValues { it.value.toMutableSet() }.toMutableMap()
        actions = data.actions.mapValues { it.value.toMutableSet() }.toMutableMap()
        properties = data.properties.mapValues { it.value.toMutableSet() }.toMutableMap()
        skillBoosts = data.skillBoosts.mapValues { it.value.toMutableSet() }.toMutableMap()
        uses = data.uses.mapValues { it.value.toMutableSet() }.toMutableMap()
        chaosTokens = data.chaosTokens.mapValues { it.value.toMutableSet() }.toMutableMap()
        tags = data.tags.mapValues { it.value.toMutableSet() }.toMutableMap()
        requiredCards = data.requiredCards.mapValues { it.value.toMutableSet() }.toMutableMap()
        sideDeckRequiredCards = data.sideDeckRequiredCards.mapValues { it.value.toMutableSet() }.toMutableMap()
        restrictedTo = data.restrictedTo.mapValues { it.value.toMutableSet() }.toMutableMap()
        advanced = data.advanced.mapValues { it.value.toMutableSet() }.toMutableMap()
        replacement = data.replacement.mapValues { it.value.toMutableSet() }.toMutableMap()
        parallelCards = data.parallelCards.mapValues { it.value.toMutableSet() }.toMutableMap()
        parallel = data.parallel.mapValues { it.value.toMutableSet() }.toMutableMap()
        base = data.base.mapValues { it.value.toMutableSet() }.toMutableMap()
        duplicates = data.duplicates.mapValues { it.value.toMutableSet() }.toMutableMap()
        reprints = data.reprints.mapValues { it.value.toMutableSet() }.toMutableMap()
        level = data.level.mapValues { it.value.toMutableSet() }.toMutableMap()
        bound = data.bound.mapValues { it.value.toMutableSet() }.toMutableMap()
        bonded = data.bonded.mapValues { it.value.toMutableSet() }.toMutableMap()
        fronts = data.fronts.mapValues { it.value.toMutableSet() }.toMutableMap()
        otherVersions = data.otherVersions.mapValues { it.value.toMutableSet() }.toMutableMap()
        basePrints = data.basePrints.mapValues { it.value.toMutableSet() }.toMutableMap()
    }
}