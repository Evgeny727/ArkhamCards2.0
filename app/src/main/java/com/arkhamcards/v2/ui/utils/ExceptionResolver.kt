package com.arkhamcards.v2.ui.utils

import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.exceptions.ClearCardsDatabaseException
import com.arkhamcards.v2.domain.exceptions.UnableCreateCardsCacheException
import com.arkhamcards.v2.domain.exceptions.UnableToLoadCardsCacheException

internal fun Throwable.resolveExceptionToStringResId(): Int? = when (this) {
    is ClearCardsDatabaseException -> R.string.clear_cards_database_error
    is UnableToLoadCardsCacheException -> R.string.load_cards_cache_error
    is UnableCreateCardsCacheException -> R.string.create_cards_cache_error
    else -> {
        if (this.localizedMessage != null) return null
        R.string.unknown_error
    }
}