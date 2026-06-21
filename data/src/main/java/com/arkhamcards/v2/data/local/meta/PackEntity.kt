package com.arkhamcards.v2.data.local.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pack",
    foreignKeys = [
        ForeignKey(
            entity = CycleEntity::class,
            parentColumns = ["code"],
            childColumns = ["cycle_code"],
            deferred = true
        )
    ],
    indices = [
        Index("cycle_code")
    ]
)
data class PackEntity(
    @PrimaryKey val code: String,
    @ColumnInfo("cycle_code")
    val cycleCode: String,
    @ColumnInfo("real_name")
    val realName: String,
    val name: String,
    val position: Int,
    val official: Boolean,
    val reprint: Boolean?,
    val chapter: Int?
)
