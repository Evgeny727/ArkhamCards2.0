package com.arkhamcards.v2.data.remote

import com.apollographql.apollo.ApolloClient
import com.arkhamcards.v2.GetCardsUpdatedAtQuery
import com.arkhamcards.v2.GetEncounterCardsQuery
import com.arkhamcards.v2.GetPlayerCardsQuery
import com.arkhamcards.v2.GetTranslationDataQuery
import javax.inject.Inject

const val CARDS_SCHEMA_VERSION = 9

class CardsRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun fetchAllTranslationData(locale: String) = apolloClient
        .query(GetTranslationDataQuery(locale))
        .execute()

    suspend fun fetchAllPlayerCards(locale: String) = apolloClient
        .query(GetPlayerCardsQuery(locale, CARDS_SCHEMA_VERSION))
        .execute()

    suspend fun fetchAllEncounterCards(locale: String) = apolloClient
        .query(GetEncounterCardsQuery(locale, CARDS_SCHEMA_VERSION))
        .execute()

    suspend fun fetchCardsUpdatedAt(locale: String) = apolloClient
        .query(GetCardsUpdatedAtQuery(locale, CARDS_SCHEMA_VERSION))
        .execute()
}
