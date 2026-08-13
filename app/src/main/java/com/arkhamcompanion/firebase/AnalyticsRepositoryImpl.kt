package com.arkhamcompanion.firebase

import com.arkhamcompanion.domain.repository.AnalyticsRepository
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val analytics: FirebaseCrashlytics
) : AnalyticsRepository {
    override fun logMessage(message: String) {
        analytics.log(message)
    }

    override fun logError(throwable: Throwable) {
        analytics.recordException(throwable)
    }

    override fun logErrorWithKeys(
        throwable: Throwable,
        keys: Map<String, Any?>,
    ) {
        val keysBuilder = CustomKeysAndValues.Builder()

        keys.forEach { (key, value) ->
            keysBuilder.putString(key, value.toString())
        }

        analytics.recordException(throwable, keysBuilder.build())
    }
}