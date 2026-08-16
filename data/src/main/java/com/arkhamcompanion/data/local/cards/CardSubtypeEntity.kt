package com.arkhamcompanion.data.local.cards

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "card_subtype")
data class CardSubtypeEntity(
    @PrimaryKey val code: String,
    val name: String,
)
