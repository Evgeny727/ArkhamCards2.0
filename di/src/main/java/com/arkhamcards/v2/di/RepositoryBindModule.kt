package com.arkhamcards.v2.di

import com.arkhamcards.v2.data.repository.CardsRepositoryImpl
import com.arkhamcards.v2.domain.repository.CardsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindModule {
    @Binds
    fun bindCardsRepository(impl: CardsRepositoryImpl): CardsRepository
}