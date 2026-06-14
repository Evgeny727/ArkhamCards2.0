package com.arkhamcards.v2.data.local.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cycle")
data class CycleEntity(
    @PrimaryKey val code: String,
    @ColumnInfo("real_name")
    val realName: String,
    val name: String,
    val position: Int,
    val official: Boolean,
    val chapter: Int?
)
