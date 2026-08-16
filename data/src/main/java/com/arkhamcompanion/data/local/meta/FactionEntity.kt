package com.arkhamcompanion.data.local.meta

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "faction")
data class FactionEntity(
    @PrimaryKey val code: String,
    val name: String,
)
