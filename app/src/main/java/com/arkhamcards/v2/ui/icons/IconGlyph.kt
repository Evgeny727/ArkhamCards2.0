package com.arkhamcards.v2.ui.icons

import androidx.compose.ui.text.font.FontFamily

interface IconGlyph {
    val stringCode: String
    val code: Int
    val glyph: String
    val fontFamily: FontFamily
}