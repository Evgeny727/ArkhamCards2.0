package com.arkhamcompanion.data.objects

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

object JsonElementConverter {
    @ColumnTypeConverter
    fun fromJsonElement(jsonElement: JsonElement?): String? {
        return jsonElement?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toJsonElement(json: String?): JsonElement? {
        return json?.let { Json.decodeFromString(it) }
    }
}