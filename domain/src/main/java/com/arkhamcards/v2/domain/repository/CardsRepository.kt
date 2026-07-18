package com.arkhamcards.v2.domain.repository

import androidx.paging.PagingData
import com.arkhamcards.v2.domain.model.cards.CardListItem
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun downloadAllCards(locale: String, onProgress: (Float) -> Unit): Result<String>

    suspend fun isCardsTableExists(): Boolean

    suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?, forced: Boolean): Result<Boolean>

    suspend fun loadCache(): Boolean

    fun searchPaginatedCardsFlow(): Flow<PagingData<CardListItem>>

}