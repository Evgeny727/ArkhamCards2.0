package com.arkhamcompanion.data.local.cards

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "card_type")
data class CardTypeEntity(
    @PrimaryKey val code: String,
    val name: String,
)
