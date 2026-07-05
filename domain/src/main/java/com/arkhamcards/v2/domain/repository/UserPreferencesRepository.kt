package com.arkhamcards.v2.domain.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Int>
    val isIncludeEnglishSearchResults: Flow<Boolean>
    val tabooSetId: Flow<String>
    val collection: Flow<ImmutableList<String>>
    val cardsUpdatedAt: Flow<String>
    val sortOrderPlayer: Flow<ImmutableList<String>>
    val sortOrderMythos: Flow<ImmutableList<String>>

    suspend fun saveThemePreference(theme: Int)
    suspend fun saveIncludeEnglishSearchResults(isIncludeEnglishSearchResults: Boolean)
    suspend fun saveTabooSetPreference(tabooSetId: String)
    suspend fun saveCollectionPreference(collection: List<String>)
    suspend fun saveCardsUpdatedTimestamp(timestamp: String)
    suspend fun saveSortOrderPlayerPreference(sortOrder: List<String>)
    suspend fun saveSortOrderMythosPreference(sortOrder: List<String>)
}