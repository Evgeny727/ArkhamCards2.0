package com.arkhamcompanion.domain.enums

enum class Gender() {
    MALE, FEMALE, NON_BINARY;

    companion object {
        fun fromCode(code: String?): Gender? {
            return when(code) {
                "m" -> MALE
                "f" -> FEMALE
                "nb" -> NON_BINARY
                else -> null
            }
        }
    }
}