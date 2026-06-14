package com.arkhamcards.v2.data.local.meta

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faction")
data class FactionEntity(
    @PrimaryKey val code: String,
    val name: String,
)
