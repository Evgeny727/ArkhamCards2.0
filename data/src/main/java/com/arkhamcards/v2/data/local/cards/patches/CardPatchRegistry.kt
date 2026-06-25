package com.arkhamcards.v2.data.local.cards.patches

class CardPatchRegistry {

    private fun loadPatches(): List<CardPatch> = loadAdditionalPatches() + loadGameBeginAttributePatches() +
            loadHiddenFixesPatches() + loadMissingTagsPatches() + loadPerInvestigatorPatches() +
            loadReprintsPatches() + loadBackTypePatches()
    private val patchesByCode: Map<String, CardPatch> =
        loadPatches().groupBy { it.code }
            .mapValues { (_, list) -> list.reduce(CardPatch::merge) }

    fun resolve(code: String): CardPatch = patchesByCode[code] ?: CardPatch(code)
}