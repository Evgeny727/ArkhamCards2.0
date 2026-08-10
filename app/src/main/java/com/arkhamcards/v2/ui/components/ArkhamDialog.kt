package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arkhamcards.v2.domain.enums.Faction

@Composable
fun ArkhamDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = allowDismiss,
            dismissOnClickOutside = allowDismiss,
            usePlatformDefaultWidth = false,
        )
    ) {
        ArkhamRoundedFactionCard(
            modifier = modifier.padding(8.dp),
            faction = Faction.Neutral,
            header = { ArkhamRoundedCardHeader(
                title = title,
                faction = Faction.Neutral,
            ) },
        ) {
            content()
        }
    }
}