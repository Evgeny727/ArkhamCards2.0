package com.arkhamcards.v2.data.local.cards.patches

internal fun loadPerInvestigatorPatches(): List<CardPatch> = listOf(
    CardPatch(
        code = "03121",
        values = EntityPatch(
            doomPerInvestigator = PatchValue.Set(true),
        )
    ),
    CardPatch(
        code = "08679",
        values = EntityPatch(
            enemyEvadePerInvestigator = PatchValue.Set(true),
            enemyFightPerInvestigator = PatchValue.Set(true),
        )
    ),
    CardPatch(
        code = "72028",
        values = EntityPatch(
            shroudPerInvestigator = PatchValue.Set(true),
            shroud = PatchValue.Set(4),
        )
    ),
    CardPatch(
        code = "87009",
        values = EntityPatch(
            shroudPerInvestigator = PatchValue.Set(true),
            shroud = PatchValue.Set(1),
        )
    ),
    CardPatch(
        code = "87018",
        values = EntityPatch(
            shroudPerInvestigator = PatchValue.Set(true),
            shroud = PatchValue.Set(1),
        )
    ),
    CardPatch(
        code = "87027",
        values = EntityPatch(
            shroudPerInvestigator = PatchValue.Set(true),
            shroud = PatchValue.Set(1),
        )
    ),
)