package com.arkhamcards.v2.domain.enums

enum class MulliganAttribute {
    STARTS_IN_HAND, STARTS_IN_PLAY, STICKY_MULLIGAN, NONE;

    fun valueByCode(code: String): MulliganAttribute = when (code) {
        "starts_in_hand" -> STARTS_IN_HAND
        "starts_in_play" -> STARTS_IN_PLAY
        "sticky_mulligan" -> STICKY_MULLIGAN
        else -> NONE
    }
}