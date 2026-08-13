package com.arkhamcompanion.domain.enums

enum class CardSubType {
    BasicWeakness, Weakness, Unknown;

    companion object {
        fun bySubType(subType: String) = when (subType) {
            "basicweakness" -> BasicWeakness
            "weakness" -> Weakness
            else -> Unknown
        }
    }
}