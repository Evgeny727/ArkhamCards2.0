package com.arkhamcards.v2.domain.repository

interface PerformanceRepository {
    suspend fun <T> trace(
        name: String,
        block: suspend () -> T
    ): T
}