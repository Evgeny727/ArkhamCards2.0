package com.arkhamcompanion.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.icons.IconGlyph
import com.arkhamcompanion.ui.theme.AppIconsFont
import com.arkhamcompanion.ui.theme.CustomTheme

@Composable
fun ArkhamToggleButton(
    checked: Boolean,
    iconGlyph: IconGlyph,
    modifier: Modifier = Modifier,
    size: TextUnit = 24.sp,
    onCheckedChange: (Boolean) -> Unit,
) {
    Surface(
        modifier = modifier.clip(CustomTheme.shapes.circle)
            .clickable { onCheckedChange(!checked) },
        color = if (checked) CustomTheme.colors.d10 else CustomTheme.colors.l10
    ) {
        Crossfade(checked) { value ->
            if (value) {
                Text(
                    text = AppIcon.Dismiss.glyph,
                    fontFamily = AppIconsFont,
                    fontSize = size,
                    color = CustomTheme.colors.l10,
                    modifier = Modifier.padding(2.dp)
                )
            } else {
                Text(
                    text = iconGlyph.glyph,
                    fontFamily = iconGlyph.fontFamily,
                    fontSize = size,
                    color = CustomTheme.colors.d10,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}