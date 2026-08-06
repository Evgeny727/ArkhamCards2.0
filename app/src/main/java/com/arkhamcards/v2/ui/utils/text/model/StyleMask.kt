package com.arkhamcards.v2.ui.utils.text.model

object StyleMask {

    const val BOLD = 1 shl 0

    const val ITALIC = 1 shl 1

    const val UNDERLINE = 1 shl 2

    const val STRIKE = 1 shl 3

    const val TRAIT = 1 shl 4

    const val RED = 1 shl 5

    const val SMALL_CAPS = 1 shl 6

    const val MINI_CAPS = 1 shl 7

    const val TYPEWRITER = 1 shl 8

    const val GAME = 1 shl 9

    const val FANCY = 1 shl 10

    const val INNSMOUTH = 1 shl 11
}

internal fun Int.has(flag: Int) = this and flag != 0
