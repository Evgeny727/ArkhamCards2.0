package com.arkhamcards.v2.data.repository

import androidx.room.withTransaction
import com.arkhamcards.v2.data.local.ArkhamDatabase
import com.arkhamcards.v2.data.local.cards.patches.CardPatchRegistry
import com.arkhamcards.v2.data.mapper.db.toEntity
import com.arkhamcards.v2.data.remote.CardsRemoteDataSource
import com.arkhamcards.v2.domain.TimestampNormilizer
import com.arkhamcards.v2.domain.model.cards.CardsUpdatedAt
import com.arkhamcards.v2.domain.repository.CardsRepository
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
    private val cardsRemoteDataSource: CardsRemoteDataSource,
    private val db: ArkhamDatabase,
) : CardsRepository {

    private val cardsDao = db.cardsDao()
    private val metaDao = db.metaDao()

    override suspend fun downloadAllCards(locale: String) = runCatching {
        val translationData = cardsRemoteDataSource.fetchAllTranslationData(locale).dataAssertNoErrors
        val playerCards = cardsRemoteDataSource.fetchAllPlayerCards(locale).dataAssertNoErrors
        val encounterCards = cardsRemoteDataSource.fetchAllEncounterCards(locale).dataAssertNoErrors

        val cardPatches = CardPatchRegistry()

        val playerEntities = playerCards.all_card.map {
            it.singleCard.toEntity(
                it.translations[0].coreCardText,
                cardPatches.resolve(it.singleCard.code
                ))
        }
        val encounterEntities = encounterCards.all_card.map {
            it.singleCard.toEntity(
                it.translations[0].coreCardText,
                cardPatches.resolve(it.singleCard.code)
            )
        }
        val cardTypeEntities = translationData.card_type_name.map { it.toEntity() }
        val cardSubtypeEntities = translationData.card_subtype_name.map { it.toEntity() }
        val factionEntities = translationData.faction_name.map { it.toEntity() }
        val cycleEntities = translationData.cycle.map {
            it.cycle.toEntity(it.translations[0].name)
        }
        val packEntities = translationData.cycle.flatMap { cycle ->
            cycle.packs.map { it.pack.toEntity(it.translations[0].name) }
        }
        val encounterSetEntities = translationData.card_encounter_set.map { it.encounterSet.toEntity() }
        val tabooSetEntities = playerCards.taboo_set.map { it.tabooSet.toEntity() }

        db.withTransaction {
            metaDao.upsertFactions(factionEntities)
            metaDao.upsertCycles(cycleEntities)
            metaDao.upsertPacks(packEntities)
            metaDao.upsertEncounterSets(encounterSetEntities)
            metaDao.upsertTabooSets(tabooSetEntities)
            cardsDao.upsertCardTypes(cardTypeEntities)
            cardsDao.upsertCardSubtypes(cardSubtypeEntities)
            cardsDao.upsertAllCards(playerEntities + encounterEntities)
        }

        val updatedAt = playerCards.all_card_updated_by_version.getOrNull(0)
        CardsUpdatedAt(
            updatedAt?.cards_updated_at.toString(),
            updatedAt?.translation_updated_at.toString(),
        )
    }

    override suspend fun isCardsTableExists(): Boolean = cardsDao.isExists()

    override suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?) = runCatching {
        val cardsUpdatedAt = cardsRemoteDataSource.fetchCardsUpdatedAt(locale).dataAssertNoErrors
            .all_card_updated.getOrNull(0)
        TimestampNormilizer.compareTimestamps(
            savedTimestamp,
            cardsUpdatedAt?.cards_updated_at.toString()
        ) || TimestampNormilizer.compareTimestamps(
            savedTimestamp,
            cardsUpdatedAt?.translation_updated_at.toString()
        )
    }
}