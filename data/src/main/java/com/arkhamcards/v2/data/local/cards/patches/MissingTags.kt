package com.arkhamcards.v2.data.local.cards.patches

import kotlinx.serialization.json.buildJsonArray

internal fun loadMissingTagsPatches(): List<CardPatch> = listOf(
    CardPatch(
        code = "04005",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hd", "hh") }),
        )
    ),
    CardPatch(
        code = "05001",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hh") }),
        )
    ),
    CardPatch(
        code = "10015",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hd", "hh") }),
        )
    ),
    CardPatch(
        code = "60501",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hd", "hh") }),
        )
    ),
    CardPatch(
        code = "09004",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hd") }),
        )
    ),
    CardPatch(
        code = "90017",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("hd") }),
        )
    ),
    CardPatch(
        code = "90081",
        values = EntityPatch(
            tags = PatchValue.Set(buildJsonArray { listOf("se") }),
        )
    ),
)