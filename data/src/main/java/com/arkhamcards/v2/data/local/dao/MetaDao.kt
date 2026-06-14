package com.arkhamcards.v2.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.arkhamcards.v2.data.local.meta.CycleEntity
import com.arkhamcards.v2.data.local.meta.EncounterSetEntity
import com.arkhamcards.v2.data.local.meta.FactionEntity
import com.arkhamcards.v2.data.local.meta.PackEntity
import com.arkhamcards.v2.data.local.meta.TabooSetEntity

@Dao
interface MetaDao {
    @Upsert
    suspend fun upsertCycles(cycles: List<CycleEntity>)

    @Upsert
    suspend fun upsertPacks(packs: List<PackEntity>)

    @Upsert
    suspend fun upsertEncounterSets(encounterSets: List<EncounterSetEntity>)

    @Upsert
    suspend fun upsertTabooSets(tabooSets: List<TabooSetEntity>)

    @Upsert
    suspend fun upsertFactions(factions: List<FactionEntity>)
}
