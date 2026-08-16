package com.arkhamcompanion.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.arkhamcompanion.ui.icons.IconGlyph

@Composable
fun ArkhamIconText(
    iconGlyph: IconGlyph,
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    Text(
        text = iconGlyph.glyph,
        modifier = modifier,
        fontFamily = iconGlyph.fontFamily,
        fontSize = with(density) {
            size.toSp()
        },
        color = color,
        maxLines = 1,
    )
}

@Composable
fun ArkhamScalableIconText(
    iconGlyph: IconGlyph,
    size: TextUnit,
    color: Color,
    modifier: Modifier = Modifier,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = iconGlyph.glyph,
        modifier = modifier,
        fontFamily = iconGlyph.fontFamily,
        fontSize = size,
        color = color,
        textDecoration = textDecoration,
        maxLines = 1,
    )
}