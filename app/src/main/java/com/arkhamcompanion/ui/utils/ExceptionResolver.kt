package com.arkhamcompanion.ui.utils

import com.arkhamcompanion.R
import com.arkhamcompanion.domain.exceptions.ClearCardsDatabaseException
import com.arkhamcompanion.domain.exceptions.UnableCreateCardsCacheException
import com.arkhamcompanion.domain.exceptions.UnableToLoadCardsCacheException

internal fun Throwable.resolveExceptionToStringResId(): Int? = when (this) {
    is ClearCardsDatabaseException -> R.string.clear_cards_database_error
    is UnableToLoadCardsCacheException -> R.string.load_cards_cache_error
    is UnableCreateCardsCacheException -> R.string.create_cards_cache_error
    else -> {
        if (this.localizedMessage != null) return null
        R.string.unknown_error
    }
}