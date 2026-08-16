package com.arkhamcompanion.data.local.cards

import androidx.room3.ColumnInfo

data class CodeWithTabooEntity(
    val code: String,
    @ColumnInfo(name = "taboo_set_id")
    val tabooSetId: Int?
)
