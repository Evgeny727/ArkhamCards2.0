package com.arkhamcards.v2.firebase

import com.arkhamcards.v2.domain.repository.PerformanceRepository
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.trace
import javax.inject.Inject

class PerformanceRepositoryImpl @Inject constructor(
    private val performance: FirebasePerformance
) : PerformanceRepository {

    override suspend fun <T> trace(name: String, block: suspend () -> T): T {
        val trace = performance.newTrace(name)

        return trace.trace {
            block()
        }
    }
}