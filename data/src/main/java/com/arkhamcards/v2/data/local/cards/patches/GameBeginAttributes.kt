package com.arkhamcards.v2.data.local.cards.patches

internal fun loadGameBeginAttributePatches(): List<CardPatch> = listOf(
    CardPatch(
        code = "05014",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("starts_in_hand"),
        )
    ),
    CardPatch(
        code = "09016",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("starts_in_play"),
        )
    ),
    CardPatch(
        code = "10005",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("starts_in_play"),
        )
    ),
    CardPatch(
        code = "06013",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("starts_in_play"),
        )
    ),
    CardPatch(
        code = "03009",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("starts_in_play"),
        )
    ),
    CardPatch(
        code = "54015",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("sticky_mulligan"),
        )
    ),
    CardPatch(
        code = "05042",
        values = EntityPatch(
            gameBeginAttribute = PatchValue.Set("sticky_mulligan"),
        )
    ),
)