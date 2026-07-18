package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamRoundedCardHeader(
    title: String,
    faction: Faction,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        color = factionColor(faction).background,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = CustomTheme.typography.header,
                color = Color.White,
            )
        }
    }
}

@ReadOnlyComposable
@Composable
fun factionColor(faction: Faction) = with(CustomTheme.colors.faction) {
    when (faction) {
        Faction.Guardian -> guardian
        Faction.Seeker -> seeker
        Faction.Rogue -> rogue
        Faction.Mystic -> mystic
        Faction.Survivor -> survivor
        Faction.Neutral -> neutral
        Faction.Mythos -> mythos
        Faction.Dual -> dual
        Faction.Dead -> dead
    }
}