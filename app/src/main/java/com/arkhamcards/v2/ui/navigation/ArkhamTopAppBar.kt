package com.arkhamcards.v2.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.ui.icons.IconGlyph
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamTopAppBar(
    title: String,
    subtitle: String?,
    color: Color,
    contentColor: Color,
    leftAction: @Composable ((Color) -> Unit)?,
    rightActions: @Composable (RowScope.(Color) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = color,
        shadowElevation = 4.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            leftAction?.invoke(contentColor)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = contentColor,
                    style = CustomTheme.typography.header,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        text = subtitle,
                        color = contentColor,
                        style = CustomTheme.typography.run { small + italic },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (rightActions != null) rightActions(contentColor)
        }
    }
}

@Composable
fun ArkhamAppBarAction(
    contentColor: Color,
    onClick: () -> Unit,
    iconGlyph: IconGlyph? = null,
    text: String? = null
) {
    if (iconGlyph != null) Box(
        modifier = Modifier.size(32.dp).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconGlyph.glyph,
            fontFamily = iconGlyph.fontFamily,
            fontSize = 28.sp,
            color = contentColor
        )
    } else if (text != null)  Box(
        modifier = Modifier.padding(4.dp).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CustomTheme.typography.text,
            color = contentColor,
        )
    }
}