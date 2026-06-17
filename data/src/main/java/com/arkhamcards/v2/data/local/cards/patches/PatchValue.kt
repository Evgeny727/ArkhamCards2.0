package com.arkhamcards.v2.data.local.cards.patches

sealed interface PatchValue<out T> {
    data object Unset : PatchValue<Nothing>
    data class Set<T>(val value: T?) : PatchValue<T>
}

fun <T> PatchValue<T>.merge(other: PatchValue<T>): PatchValue<T> =
    when (other) {
        PatchValue.Unset -> this
        is PatchValue.Set -> other
    }

fun <T> PatchValue<T>.resolve(current: T? = null): T? =
    when (this) {
        PatchValue.Unset -> current
        is PatchValue.Set -> value
    }