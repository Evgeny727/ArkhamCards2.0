package com.arkhamcompanion.data.local.cards

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_subtype")
data class CardSubtypeEntity(
    @PrimaryKey val code: String,
    val name: String,
)
