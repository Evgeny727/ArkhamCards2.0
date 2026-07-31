package com.arkhamcards.v2.data.local.cards

import androidx.room.ColumnInfo

data class CodeWithTabooEntity(
    val code: String,
    @ColumnInfo(name = "taboo_set_id")
    val tabooSetId: Int?
)
