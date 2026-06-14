package com.arkhamcards.v2.domain.repository

import com.arkhamcards.v2.domain.model.cards.CardsUpdatedAt

interface CardsRepository {

    suspend fun downloadAllCards(locale: String): Result<CardsUpdatedAt>

    suspend fun isCardsTableExists(): Boolean

    suspend fun isCardsUpdateAvailable(locale: String, savedTimestamp: String?): Result<Boolean>

}