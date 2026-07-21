package com.arkhamcards.v2.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont

private val ICON_REGEX = Regex("""\[(.*?)]""")

fun String.iconize(iconSize: TextUnit, color: Color? = null): AnnotatedString {
    return buildAnnotatedString {
        var lastIndex = 0

        ICON_REGEX.findAll(this@iconize).forEach { match ->
            append(this@iconize.substring(lastIndex, match.range.first))

            val iconName = match.groupValues[1]

            withStyle(
                style = SpanStyle(
                    color = color ?: Color.Unspecified,
                    fontFamily = AppIconsFont,
                    fontSize = iconSize
                )
            ) {
                append(AppIcon.fromNameCode(iconName).glyph)
            }

            lastIndex = match.range.last + 1
        }

        append(this@iconize.substring(lastIndex))
    }
}