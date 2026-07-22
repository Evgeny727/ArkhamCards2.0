package com.arkhamcards.v2.ui.objects

import androidx.annotation.StringRes
import com.arkhamcards.v2.R
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

data class SortOption(
    val id: String,
    @StringRes val resId: Int,
    val isActive: Boolean = false
)

object CardsSortOptions {
    fun getSortOptions(spoiler: Boolean): ImmutableMap<String, Int> {
        val map = mapOf(
            "type" to R.string.sort_by_type,
            "slot" to R.string.sort_by_slot,
            "faction" to R.string.sort_by_faction,
            "cost" to R.string.sort_by_cost,
            "level" to R.string.sort_by_level,
            "cycle" to R.string.sort_by_cycle,
            "pack" to R.string.sort_by_pack,
            "name" to R.string.sort_by_name,
            "position" to R.string.sort_by_position,
        ) + if (spoiler) mapOf(
            "encounter" to R.string.sort_by_encounter,
        ) else emptyMap()
        return map.toImmutableMap()
    }
}