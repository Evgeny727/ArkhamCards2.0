package com.arkhamcards.v2.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomRawQuery
import androidx.room.withTransaction
import com.arkhamcards.v2.data.local.ArkhamDatabase
import com.arkhamcards.v2.data.local.cards.CardCacheData
import com.arkhamcards.v2.data.local.cards.patches.CardPatchRegistry
import com.arkhamcards.v2.data.mapper.db.toData
import com.arkhamcards.v2.data.mapper.db.toEntity
import com.arkhamcards.v2.data.mapper.domain.cards.toDomain
import com.arkhamcards.v2.data.objects.CardCache
import com.arkhamcards.v2.data.objects.CardCache.createCache
import com.arkhamcards.v2.data.remote.CardsRemoteDataSource
import com.arkhamcards.v2.domain.TimestampNormilizer.compareTimestamps
import com.arkhamcards.v2.domain.TimestampNormilizer.getCurrentDateTime
import com.arkhamcards.v2.domain.TimestampNormilizer.isAtLeastWeekApart
import com.arkhamcards.v2.domain.model.cards.CardListItem
import com.arkhamcards.v2.domain.model.cards.CardsSearchPreferences
import com.arkhamcards.v2.domain.repository.CardsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import javax.inject.Inject

private val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

class CardsRepositoryImpl @Inject constructor(
    private val cardsRemoteDataSource: CardsRemoteDataSource,
    private val db: ArkhamDatabase,
    @ApplicationContext private val context: Context
) : CardsRepository {

    private val cardsDao = db.cardsDao()
    private val metaDao = db.metaDao()

    override suspend fun downloadAllCards(locale: String, onProgress: (Float) -> Unit) = runCatching {
        val translationData = cardsRemoteDataSource.fetchAllTranslationData(locale).dataAssertNoErrors
        onProgress(0.15f)
        val playerCards = cardsRemoteDataSource.fetchAllPlayerCards(locale).dataAssertNoErrors
        onProgress(0.30f)
        val encounterCards = cardsRemoteDataSource.fetchAllEncounterCards(locale).dataAssertNoErrors
        onProgress(0.45f)

        val cardPatches = CardPatchRegistry()

        val cardTypeEntities = translationData.card_type_name.map { it.toEntity() }
        val cardSubtypeEntities = translationData.card_subtype_name.map { it.toEntity() }
        val factionEntities = translationData.faction_name.map { it.toEntity() }
        val cycleEntities = translationData.cycle.map {
            it.cycle.toEntity(it.translations.getOrNull(0)?.name ?: it.cycle.real_name)
        }
        val packEntities = translationData.cycle.flatMap { cycle ->
            cycle.packs.map {
                it.pack.toEntity(it.translations.getOrNull(0)?.name ?: it.pack.real_name)
            }
        }
        val translatedEncountersMap = translationData.card_encounter_set.associateBy { it.encounterSet.code }
        val encounterSetEntities = translationData.english_encounters.map {
            it.encounterSet.toEntity(translatedEncountersMap[it.encounterSet.code]?.encounterSet)
        }
        val tabooSetEntities = playerCards.taboo_set.map { it.tabooSet.toEntity() }

        val packMap = packEntities.associateBy { it.code }
        val cycleMap = cycleEntities.associateBy { it.code }

        val playerEntities = playerCards.all_card.map {
            val pack = packMap[it.singleCard.pack_code]!!
            val cycle = cycleMap[pack.cycleCode]!!

            it.singleCard.toEntity(
                it.translations.getOrNull(0)?.coreCardText,
                cardPatches.resolve(it.singleCard.code),
                cycle,
                packMap[it.singleCard.pack_code]!!,
                locale
            )
        }
        onProgress(0.50f)
        val encounterEntities = encounterCards.all_card.map {
            val pack = packMap[it.singleCard.pack_code]!!
            val cycle = cycleMap[pack.cycleCode]!!

            it.singleCard.toEntity(
                it.translations.getOrNull(0)?.coreCardText,
                cardPatches.resolve(it.singleCard.code),
                cycle,
                packMap[it.singleCard.pack_code]!!,
                locale
            )
        }
        onProgress(0.55f)

        val allCards = playerEntities + encounterEntities

        db.withTransaction {
            cardsDao.deleteAllCards()
            metaDao.deleteAll()
            metaDao.upsertFactions(factionEntities)
            metaDao.upsertCycles(cycleEntities)
            metaDao.upsertPacks(packEntities)
            metaDao.upsertEncounterSets(encounterSetEntities)
            Log.e("transaction", "upsertEncounterSets: ${encounterSetEntities.size}")
            metaDao.upsertTabooSets(tabooSetEntities)
            cardsDao.upsertCardTypes(cardTypeEntities)
            cardsDao.upsertCardSubtypes(cardSubtypeEntities)
            cardsDao.upsertAllCards(allCards)
        }
        onProgress(0.85f)

        createCache(allCards)
        onProgress(0.93f)
        saveCache()
        onProgress(0.97f)

        val updatedAt = playerCards.all_card_updated_by_version.getOrNull(0)
        val compared = compareTimestamps(
            updatedAt?.cards_updated_at.toString(),
            updatedAt?.translation_updated_at.toString(),
        )

        onProgress(1.0f)

        if (compared) updatedAt?.translation_updated_at.toString()
        else updatedAt?.cards_updated_at.toString()
    }

    override suspend fun isCardsTableExists(): Boolean = cardsDao.isExists()

    override suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?, forced: Boolean) = runCatching {
        val currentTimestamp = getCurrentDateTime()
        if (!forced && !isAtLeastWeekApart(savedTimestamp, currentTimestamp))
            return@runCatching false

        val cardsUpdatedAt = cardsRemoteDataSource.fetchCardsUpdatedAt(locale).dataAssertNoErrors
            .all_card_updated.getOrNull(0)

        compareTimestamps(
            savedTimestamp,
            cardsUpdatedAt?.cards_updated_at.toString()
        ) || compareTimestamps(
            savedTimestamp,
            cardsUpdatedAt?.translation_updated_at.toString()
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun saveCache() = withContext(Dispatchers.IO) {
        File(context.filesDir, "card_cache.json")
            .outputStream()
            .buffered()
            .use { json.encodeToStream(CardCache.toData(), it) }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadCache(): Boolean = withContext(Dispatchers.IO) {

        val file = File(context.filesDir, "card_cache.json")

        if (!file.exists()) { return@withContext false }

        val data: CardCacheData =
            file.inputStream().buffered().use(json::decodeFromStream)

        CardCache.load(data)

        return@withContext true
    }

    override fun searchPaginatedCardsFlow(
        spoilerState: Boolean,
        searchQuery: String,
        searchPreferences: CardsSearchPreferences
    ): Flow<PagingData<CardListItem>> {
        val rawQuery = buildSearchCardsQuery(spoilerState, searchQuery, searchPreferences)

        return Pager(
            config = PagingConfig(
                pageSize = 70,
                prefetchDistance = 140,
                enablePlaceholders = false,
                initialLoadSize = 300,
                maxSize = 600
            ),
            pagingSourceFactory = { cardsDao.searchCardsRaw(rawQuery) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    private fun buildSearchCardsQuery(
        spoilerState: Boolean,
        searchQuery: String,
        searchPreferences: CardsSearchPreferences
    ): RoomRawQuery {
        return RoomRawQuery(
            sql = """
                WITH filtered_cards AS (
                    WITH selected_taboo AS (
                        SELECT CASE
                            WHEN ? = 0 THEN NULL
                            WHEN ? = 100 THEN (SELECT MAX(id) FROM taboo_set)
                            ELSE ?
                        END AS id
                    )
                    
                    SELECT
                        c.id,
                        c.code,
                        c.duplicate_of_code,
                        c.thumbnailurl,
                    
                        c.cost,
                        c.xp,
                        c.permanent,
                    
                        c.taboo_xp,
                        c.taboo_set_id,
                    
                        c.type_code,
                        t.name AS typeName,
                    
                        c.subtype_code,
                        st.name AS subTypeName,
                    
                        c.faction_code,
                        f.name AS factionName,
                        c.faction2_code,
                        c.faction3_code,
                    
                        c.pack_code,
                        p.name AS packName,
                        c.pack_position,
                    
                        c.encounter_code,
                        e.name AS encounterName,
                    
                        c.cycle_code,
                        cy.name AS cycleName,
                        
                        c.reprint_pack_code,
                    
                        c.name,
                        c.subname,
                    
                        c.skill_willpower,
                        c.skill_intellect,
                        c.skill_combat,
                        c.skill_agility,
                        c.skill_wild,
                    
                        c.parallel,
                        c.is_unique,
                        c.slot,
                        c.stage
                    FROM card c
                    JOIN card_type t
                        ON c.type_code = t.code
                    LEFT JOIN card_subtype st
                        ON c.subtype_code = st.code
                    JOIN faction f
                        ON c.faction_code = f.code
                    JOIN pack p
                        ON c.pack_code = p.code
                    LEFT JOIN encounter_set e
                        ON c.encounter_code = e.code
                    JOIN cycle cy
                        ON c.cycle_code = cy.code
                    CROSS JOIN selected_taboo taboo
                    WHERE c.encounter_code IS ${if (spoilerState) "NOT NULL" else "NULL"} AND c.hidden = 0
                    ${if (searchPreferences.showFanMade) "" else " AND (c.official = 1 AND c.preview = 0)"}
                    ${if (spoilerState) "" 
                    else """ AND
                     (
                        -- No taboo selected -> originals only
                        (taboo.id IS NULL AND c.taboo_set_id IS NULL)
        
                        OR
                        
                        (taboo.id IS NOT NULL AND 
                            (
                                -- Selected taboo version
                                c.taboo_set_id = taboo.id
                
                                OR
                
                                -- Original version if no taboo override exists
                                (c.taboo_set_id IS NULL
                                    AND NOT EXISTS (
                                        SELECT 1 FROM card t WHERE t.taboo_set_id = taboo.id AND t.code = c.code
                                    )
                                )
                            )
                        )
                     )
                    """.trimIndent() 
                    }
                ),
                
                ranked_cards AS (
                    SELECT *, ROW_NUMBER() OVER (
                        PARTITION BY
                            COALESCE(duplicate_of_code, code)
                        ORDER BY
                            CASE
                                WHEN duplicate_of_code IS NULL THEN 0
                                ELSE 1
                            END,
                            code
                    ) AS duplicate_rank FROM filtered_cards
                )

                SELECT * FROM ranked_cards WHERE duplicate_rank = 1
            """.trimIndent(),
            onBindStatement = { statement ->
                var index = 1
                with(searchPreferences) {
                    repeat(3) {
                        statement.bindInt(index++, tabooSetId)
                    }
                }
            }
        )
    }
}