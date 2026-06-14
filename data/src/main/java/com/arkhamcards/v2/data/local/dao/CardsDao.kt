package com.arkhamcards.v2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.arkhamcards.v2.data.local.cards.CardEntity
import com.arkhamcards.v2.data.local.cards.CardSubtypeEntity
import com.arkhamcards.v2.data.local.cards.CardTypeEntity

@Dao
interface CardsDao {
    @Upsert
    suspend fun upsertAllCards(cards: List<CardEntity>)

    @Upsert
    suspend fun upsertCardTypes(types: List<CardTypeEntity>)

    @Upsert
    suspend fun upsertCardSubtypes(subtypes: List<CardSubtypeEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM card)")
    suspend fun isExists(): Boolean
}