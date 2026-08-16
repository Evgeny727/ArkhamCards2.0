package com.arkhamcompanion.data.local.meta

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey


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
