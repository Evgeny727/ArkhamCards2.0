package com.arkhamcards.v2.data.local.meta

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "encounter_set")
data class EncounterSetEntity(
    @PrimaryKey val code: String,
    val name: String
)
