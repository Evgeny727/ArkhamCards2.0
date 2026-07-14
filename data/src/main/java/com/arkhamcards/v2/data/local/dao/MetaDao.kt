package com.arkhamcards.v2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.arkhamcards.v2.data.local.meta.CycleEntity
import com.arkhamcards.v2.data.local.meta.EncounterSetEntity
import com.arkhamcards.v2.data.local.meta.FactionEntity
import com.arkhamcards.v2.data.local.meta.PackEntity
import com.arkhamcards.v2.data.local.meta.TabooSetEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("DELETE FROM cycle")
    suspend fun deleteAllCycles()

    @Query("DELETE FROM pack")
    suspend fun deleteAllPacks()

    @Query("DELETE FROM encounter_set")
    suspend fun deleteAllEncounterSets()

    @Query("DELETE FROM taboo_set")
    suspend fun deleteAllTabooSets()

    @Query("DELETE FROM faction")
    suspend fun deleteAllFactions()

    suspend fun deleteAll() {
        deleteAllPacks()
        deleteAllCycles()
        deleteAllEncounterSets()
        deleteAllTabooSets()
        deleteAllFactions()
    }

    @Query("SELECT * FROM taboo_set ORDER BY date DESC")
    fun getTaboos(): Flow<List<TabooSetEntity>>
}
