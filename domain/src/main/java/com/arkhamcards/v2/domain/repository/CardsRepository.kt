package com.arkhamcards.v2.domain.repository

interface CardsRepository {

    suspend fun downloadAllCards(locale: String): Result<String>

    suspend fun isCardsTableExists(): Boolean

    suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?, forced: Boolean): Result<Boolean>

    suspend fun loadCache(): Boolean

}