package com.arkhamcards.v2.data.local.cards.patches

internal fun loadAdditionalPatches(): List<CardPatch> = listOf(
    CardPatch(
        code = "60169",
        values = EntityPatch(
            duplicateOf = PatchValue.Set("12025"),
            reprintOf = PatchValue.Set(null),
            preview = PatchValue.Set(null)
        )
    ),
    CardPatch(
        code = "60459",
        values = EntityPatch(
            duplicateOf = PatchValue.Set("12059"),
            reprintOf = PatchValue.Set(null),
            preview = PatchValue.Set(null)
        )
    ),
)