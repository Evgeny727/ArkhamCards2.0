package com.arkhamcards.v2.di

import com.arkhamcards.v2.data.repository.CardsRepositoryImpl
import com.arkhamcards.v2.data.repository.UserPreferencesRepositoryImpl
import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindModule {
    @Binds
    fun bindCardsRepository(impl: CardsRepositoryImpl): CardsRepository

    @Binds
    fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}