package com.arkhamcards.v2.data.objects

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { Json.decodeFromString(it) }
    }
}