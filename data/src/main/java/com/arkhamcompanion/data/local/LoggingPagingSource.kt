package com.arkhamcompanion.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arkhamcompanion.domain.repository.AnalyticsRepository

class LoggingPagingSource<Key : Any, Value : Any>(
    private val delegate: PagingSource<Key, Value>,
    private val analyticsRepository: AnalyticsRepository,
) : PagingSource<Key, Value>() {

    init {
        delegate.registerInvalidatedCallback {
            invalidate()
        }
    }

    override suspend fun load(
        params: LoadParams<Key>,
    ): LoadResult<Key, Value> {
        return delegate.load(params).also { result ->
            if (result is LoadResult.Error) {
                analyticsRepository.logError(result.throwable)
            }
        }
    }

    override fun getRefreshKey(
        state: PagingState<Key, Value>,
    ): Key? = delegate.getRefreshKey(state)
}