package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamRoundedFactionCard(
    faction: Faction,
    modifier: Modifier = Modifier,
    color: Color? = null,
    isPadded: Boolean = true,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = CustomTheme.shapes.large,
        color = CustomTheme.colors.background,
        border = BorderStroke(1.dp, color ?: factionColor(faction).background),
        shadowElevation = 6.dp
    ) {
        Column {
            header?.invoke()
            Column(
                modifier = Modifier.padding(if (isPadded) 8.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
            footer?.invoke()
        }
    }
}