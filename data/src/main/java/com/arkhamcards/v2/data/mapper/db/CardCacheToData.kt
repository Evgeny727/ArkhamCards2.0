package com.arkhamcards.v2.data.mapper.db

import com.arkhamcards.v2.data.local.cards.CardCacheData
import com.arkhamcards.v2.data.objects.CardCache

fun CardCache.toData() = CardCacheData(
    traits = traits,
    actions = actions,
    properties = properties,
    skillBoosts = skillBoosts,
    uses = uses,
    chaosTokens = chaosTokens,
    tags = tags,
    requiredCards = requiredCards,
    sideDeckRequiredCards = sideDeckRequiredCards,
    restrictedTo = restrictedTo,
    advanced = advanced,
    replacement = replacement,
    parallelCards = parallelCards,
    parallel = parallel,
    base = base,
    duplicates = duplicates,
    reprints = reprints,
    level = level,
    bound = bound,
    bonded = bonded,
    fronts = fronts,
    otherVersions = otherVersions,
    basePrints = basePrints,
)