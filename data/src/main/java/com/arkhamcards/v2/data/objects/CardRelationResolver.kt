package com.arkhamcards.v2.data.objects

import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithPackInfo
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithRelations
import com.arkhamcards.v2.domain.model.cards.CardRelations
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object CardRelationResolver {

    private data class Node(
        val code: String,
        val withRelations: Boolean,
    )

    fun resolveCardCodesWithRelations(code: String): Collection<String> {

        CardCache.relationsCache[code]?.let {
            if (it.isNotEmpty()) return@resolveCardCodesWithRelations it
        }

        val visited = mutableSetOf<String>()
        val result = mutableSetOf<String>()
        val stack = ArrayDeque<Node>()

        stack.addLast(Node(code, true))

        while (stack.isNotEmpty()) {
            val (currentCode, includeRelations) = stack.removeLast()

            if (!visited.add(currentCode)) continue

            result += currentCode

            CardCache.backs[currentCode]?.let {
                stack.addLast(Node(it, true))
            }

            if (includeRelations) {
                stack.pushRelations(currentCode, RelationType.Advanced)
                stack.pushRelations(currentCode, RelationType.Base, true)
                stack.pushRelations(currentCode, RelationType.Parallel, true)
                stack.pushRelations(currentCode, RelationType.Replacement)
                stack.pushRelations(currentCode, RelationType.RequiredCards)
                stack.pushRelations(currentCode, RelationType.SideDeckRequiredCards)
                stack.pushRelations(currentCode, RelationType.ParallelCards)
                stack.pushRelations(currentCode, RelationType.OtherVersions)
                stack.pushRelations(currentCode, RelationType.RestrictedTo)
                stack.pushRelations(currentCode, RelationType.Level)

                val restrictedToInvestigator = getRelations(currentCode, RelationType.RestrictedTo)
                restrictedToInvestigator.firstOrNull()?.let {
                    stack.pushRelations(it, RelationType.Advanced)
                    stack.pushRelations(it, RelationType.RequiredCards)
                    stack.pushRelations(it, RelationType.Replacement)
                }
            }

            stack.pushRelations(currentCode, RelationType.Duplicates)
            stack.pushRelations(currentCode, RelationType.Reprints)
            stack.pushRelations(currentCode, RelationType.Bound)
            stack.pushRelations(currentCode, RelationType.Bonded)
        }

        CardCache.setRelationsByCode(code, result)
        return result
    }

    private fun ArrayDeque<Node>.pushRelations(
        code: String,
        relationType: RelationType,
        singleRelation: Boolean = false
    ) {
        val relations = getRelations(code, relationType)
        if (singleRelation) relations.firstOrNull()?.let { addLast(Node(it, false)) }
        else relations.forEach { addLast(Node(it, false)) }
    }

    private fun getRelations(
        code: String,
        relationType: RelationType,
    ): Set<String> =
        when (relationType) {
            RelationType.Advanced -> CardCache.advanced[code]
            RelationType.Base -> CardCache.base[code]
            RelationType.Parallel -> CardCache.parallel[code]
            RelationType.Replacement -> CardCache.replacement[code]
            RelationType.RequiredCards -> CardCache.requiredCards[code]
            RelationType.SideDeckRequiredCards -> CardCache.sideDeckRequiredCards[code]
            RelationType.ParallelCards -> CardCache.parallelCards[code]
            RelationType.OtherVersions -> CardCache.otherVersions[code]
            RelationType.RestrictedTo -> CardCache.restrictedTo[code]
            RelationType.Level -> CardCache.level[code]
            RelationType.Duplicates -> CardCache.duplicates[code]
            RelationType.Reprints -> CardCache.reprints[code]
            RelationType.Bound -> CardCache.bound[code]
            RelationType.Bonded -> CardCache.bonded[code]
        } ?: emptySet()

    fun buildCardWithRelations(
        rootCode: String,
        cardsMap: Map<String, CardDetailsWithPackInfo>
    ): CardDetailsWithRelations {
        val root = cardsMap[rootCode]!!

        val back = CardCache.backs[rootCode]?.let {
            cardsMap[it]
        }


        var restrictedTo: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var parallel: CardDetailsWithPackInfo? = null
        var base: CardDetailsWithPackInfo? = null
        var advanced: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var replacement: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var requiredCards: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var sideDeckRequiredCards: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var parallelCards: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var otherVersions: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var level: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()
        var otherSignatures: ImmutableList<CardDetailsWithPackInfo> = persistentListOf()

        if (root.cardDetails.type == CardType.Investigator) {
            advanced = cardsMap.buildRelationList(rootCode, RelationType.Advanced)
            base = cardsMap.buildSingleRelation(rootCode, RelationType.Base)
            parallel = cardsMap.buildSingleRelation(rootCode, RelationType.Parallel)
            replacement = cardsMap.buildRelationList(rootCode, RelationType.Replacement)
            requiredCards = cardsMap.buildRelationList(rootCode, RelationType.RequiredCards)
            sideDeckRequiredCards = cardsMap.buildRelationList(rootCode, RelationType.SideDeckRequiredCards)
            parallelCards = cardsMap.buildRelationList(rootCode, RelationType.ParallelCards)
            otherVersions = cardsMap.buildRelationList(rootCode, RelationType.OtherVersions)
        } else {
            restrictedTo = cardsMap.buildRelationList(rootCode, RelationType.RestrictedTo)
            level = cardsMap.buildRelationList(rootCode, RelationType.Level)

            restrictedTo.firstOrNull()?.let { investigator ->
                val otherAdvanced = cardsMap.buildRelationList(
                    investigator.cardDetails.code,
                    RelationType.Advanced
                )
                val otherRequired = cardsMap.buildRelationList(
                    investigator.cardDetails.code,
                    RelationType.RequiredCards
                )
                val otherReplacement = cardsMap.buildRelationList(
                    investigator.cardDetails.code,
                    RelationType.Replacement
                )

                val duplicateCodes = getRelations(rootCode, RelationType.Duplicates) +
                        getRelations(rootCode, RelationType.Bound) + getRelations(rootCode, RelationType.Bonded)

                val seenCodes = mutableSetOf<String>()

                val matched = buildList {
                    otherAdvanced
                        .asSequence()
                        .plus(otherRequired)
                        .plus(otherReplacement)
                        .forEach { card ->
                            val code = card.cardDetails.code

                            if (
                                code != rootCode &&
                                code !in duplicateCodes &&
                                card.cardDetails.subType == root.cardDetails.subType &&
                                seenCodes.add(code)
                            ) {
                                add(card)
                            }
                        }
                }.sortedBy { it.cardDetails.name }

                otherSignatures = matched.toImmutableList()
            }
        }

        val bound: ImmutableList<CardDetailsWithPackInfo> =
            cardsMap.buildRelationList(rootCode, RelationType.Bound)
        val bonded: ImmutableList<CardDetailsWithPackInfo> =
            cardsMap.buildRelationList(rootCode, RelationType.Bonded)

        return CardDetailsWithRelations(
            cardDetails = root,
            backCardDetails = back,
            cardRelations = CardRelations(
                bound = bound,
                bonded = bonded,
                restrictedTo = restrictedTo,
                parallel = parallel,
                base = base,
                advanced = advanced,
                replacement = replacement,
                requiredCards = requiredCards,
                sideDeckRequiredCards = sideDeckRequiredCards,
                parallelCards = parallelCards,
                otherVersions = otherVersions,
                level = level,
                otherSignatures = otherSignatures,
            )
        )
    }

    private val relationComparator =
        compareBy<CardDetailsWithPackInfo>(
            {
                CardSortOrder.sortByTypeOrder(
                    it.cardDetails.type.name.lowercase(),
                    it.cardDetails.subType?.name?.lowercase()
                )
            },
            { it.cardDetails.name },
            { it.cardDetails.xp ?: -1 },
            { it.cardDetails.packPosition },
        )

    private fun Map<String, CardDetailsWithPackInfo>.buildRelationList(
        rootCode: String,
        relation: RelationType,
    ): ImmutableList<CardDetailsWithPackInfo> {
        return buildList {
            getRelations(rootCode, relation).forEach { code ->
                this@buildRelationList[code]
                    ?.takeIf { it.cardDetails.duplicateOfCode == null }
                    ?.let(::add)
            }
        }.sortedWith(relationComparator).toImmutableList()
    }

    private fun Map<String, CardDetailsWithPackInfo>.buildSingleRelation(
        rootCode: String,
        relation: RelationType,
    ): CardDetailsWithPackInfo? =
        buildRelationList(rootCode, relation).firstOrNull()
}

enum class RelationType {
    Advanced, Base, Parallel, Replacement, RequiredCards, SideDeckRequiredCards, ParallelCards,
    OtherVersions, RestrictedTo, Level, Duplicates, Reprints, Bound, Bonded
}