package com.arkhamcards.v2.ui.utils.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.SpanStyle
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.text.model.StyleMask
import com.arkhamcards.v2.ui.utils.text.model.has

internal class StyleResolver {

    private val cache = HashMap<Int, SpanStyle>()

    @ReadOnlyComposable
    @Composable
    fun style(mask: Int): SpanStyle =
        cache.getOrPut(mask) {
            buildStyle(mask)
        }

    @ReadOnlyComposable
    @Composable
    private fun buildStyle(mask: Int): SpanStyle {
        var style = SpanStyle()

        if (mask.has(StyleMask.BOLD) && mask.has(StyleMask.ITALIC))
            style = style.merge(CustomTheme.typography.boldItalic.toSpanStyle())

        if (mask.has(StyleMask.BOLD))
            style = style.merge(CustomTheme.typography.bold.toSpanStyle())

        if (mask.has(StyleMask.ITALIC))
            style = style.merge(CustomTheme.typography.italic.toSpanStyle())

        if (mask.has(StyleMask.UNDERLINE))
            style = style.merge(CustomTheme.typography.underline)

        if (mask.has(StyleMask.STRIKE))
            style = style.merge(CustomTheme.typography.strikethrough)

        if (mask.has(StyleMask.RED))
            style = style.merge(SpanStyle(color = CustomTheme.colors.red))

        return style
    }
}