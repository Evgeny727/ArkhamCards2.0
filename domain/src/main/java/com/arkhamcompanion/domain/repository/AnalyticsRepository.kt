package com.arkhamcompanion.domain.repository

interface AnalyticsRepository {

    fun logMessage(message: String)

    fun logError(throwable: Throwable)

    fun logErrorWithKeys(throwable: Throwable, keys: Map<String, Any?>)
}