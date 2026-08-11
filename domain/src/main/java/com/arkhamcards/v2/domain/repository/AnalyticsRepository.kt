package com.arkhamcards.v2.domain.repository

interface AnalyticsRepository {

    fun logMessage(message: String)

    fun logError(throwable: Throwable)

    fun logErrorWithKeys(throwable: Throwable, keys: Map<String, Any?>)
}