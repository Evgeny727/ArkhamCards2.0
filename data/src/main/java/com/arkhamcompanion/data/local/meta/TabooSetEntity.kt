package com.arkhamcompanion.data.local.meta

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "taboo_set")
data class TabooSetEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val code: String,
    val active: Boolean,
    val date: String?,
    @ColumnInfo(name = "card_count")
    val cardCount: Int?,
    val current: Boolean
)
