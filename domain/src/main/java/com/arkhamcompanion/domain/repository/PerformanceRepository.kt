package com.arkhamcompanion.domain.repository

interface PerformanceRepository {
    suspend fun <T> trace(
        name: String,
        block: suspend () -> T
    ): T
}