package com.arkhamcards.v2.firebase.di

import com.arkhamcards.v2.domain.repository.AnalyticsRepository
import com.arkhamcards.v2.domain.repository.PerformanceRepository
import com.arkhamcards.v2.firebase.AnalyticsRepositoryImpl
import com.arkhamcards.v2.firebase.PerformanceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseBindingModule {
    @Binds
    @Singleton
    fun bindAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

    @Binds
    @Singleton
    fun bindPerformanceRepository(impl: PerformanceRepositoryImpl): PerformanceRepository
}