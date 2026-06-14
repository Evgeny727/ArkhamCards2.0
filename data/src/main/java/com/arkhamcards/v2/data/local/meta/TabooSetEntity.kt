package com.arkhamcards.v2.data.local.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taboo_set")
data class TabooSetEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val code: String,
    val active: Boolean,
    val date: String?,
    @ColumnInfo(name = "card_count")
    val cardCount: Int?,
    val cards: List<String>, // List of card codes
    val current: Boolean
)
