package com.arkhamcards.v2.domain.repository

import androidx.paging.PagingData
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithRelations
import com.arkhamcards.v2.domain.model.cards.CardListItemUiModel
import com.arkhamcards.v2.domain.model.cards.CardsSearchOptions
import com.arkhamcards.v2.domain.model.cards.CardsSearchPreferences
import com.arkhamcards.v2.domain.model.cards.CodeWithTaboo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun downloadAllCards(locale: String, onProgress: (Float) -> Unit): Result<String>

    suspend fun isCardsTableExists(): Boolean

    suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?, forced: Boolean): Result<Boolean>

    suspend fun loadCache(): Boolean

    fun searchPaginatedCardsFlow(
        spoilerState: Boolean,
        searchOptions: CardsSearchOptions,
        searchPreferences: CardsSearchPreferences
    ): Flow<PagingData<CardListItemUiModel>>

    fun searchPaginatedCardCodesFlow(
        spoilerState: Boolean,
        searchOptions: CardsSearchOptions,
        searchPreferences: CardsSearchPreferences
    ): Flow<ImmutableList<CodeWithTaboo>>

    fun getCardWithRelationsByCodeFlow(
        code: String,
        tabooSetId: Int?,
    ): Flow<CardDetailsWithRelations>

}