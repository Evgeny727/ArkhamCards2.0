package com.arkhamcards.v2.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.IconGlyph
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamSurfaceButton(
    title: String,
    valueLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    editable: Boolean = true,
    icon: IconGlyph? = null,
    valueLabelDescription: String? = null,
    noLabelDivider: Boolean = false,
) {
    val iconSize = if (icon == AppIcon.PerInvestigator) 26.sp else 32.sp

    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = editable,
        shape = CustomTheme.shapes.large,
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            icon?.let {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(loading) {
                        if (it) {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp), color = CustomTheme.colors.m)
                        } else {
                            Text(
                                text = icon.glyph,
                                fontFamily = icon.fontFamily,
                                fontSize = iconSize,
                                color = CustomTheme.colors.m
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = CustomTheme.colors.d30,
                    style = CustomTheme.typography.run { smallLabel + italic },
                )
                Row {
                    Text(
                        text = valueLabel,
                        color = CustomTheme.colors.d30,
                        style = CustomTheme.typography.large,
                    )
                    valueLabelDescription?.let {
                        Text(
                            text = (if (noLabelDivider) " " else " · ") + valueLabel,
                            color = CustomTheme.colors.m,
                            style = CustomTheme.typography.small,
                        )
                    }
                }
            }

            if (editable) {
                val editIcon = AppIcon.Edit
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = editIcon.glyph,
                        fontFamily = editIcon.fontFamily,
                        fontSize = 32.sp,
                        color = CustomTheme.colors.m
                    )
                }
            }
        }
    }
}

@Composable
fun ArkhamSurfaceButtonGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CustomTheme.shapes.large,
        color = CustomTheme.colors.l20
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}