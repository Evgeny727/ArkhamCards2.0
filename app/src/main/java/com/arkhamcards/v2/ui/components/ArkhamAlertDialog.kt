package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun ArkhamAlertDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    buttons: (@Composable () -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
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
            Text(
                text = description,
                style = CustomTheme.typography.small,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            buttons?.invoke()
        }
    }
}

@Composable
fun ArkhamAlertButton(
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: @Composable ((Color) -> Unit)? = null,
    loading: Boolean = false,
    style: ArkhamAlertButtonStyle = ArkhamAlertButtonStyle.DEFAULT,
    onClick: () -> Unit,
) {
    val appIcon = when (style) {
        ArkhamAlertButtonStyle.DEFAULT -> AppIcon.CheckThin
        ArkhamAlertButtonStyle.CANCEL -> AppIcon.Dismiss
        ArkhamAlertButtonStyle.DESTRUCTIVE -> AppIcon.Trash
    }
    val finalIcon = icon ?: { color ->
        Text(
            text = appIcon.glyph,
            fontFamily = appIcon.fontFamily,
            color = color,
            fontSize = iconSize(appIcon)
        )
    }
    ArkhamSquareButton(
        title = text,
        onClick = onClick,
        modifier = modifier,
        detail = description,
        loading = loading,
        colors = when (style) {
            ArkhamAlertButtonStyle.DEFAULT -> ArkhamButtonColor.Default
            ArkhamAlertButtonStyle.CANCEL -> ArkhamButtonColor.RedOutline
            ArkhamAlertButtonStyle.DESTRUCTIVE -> ArkhamButtonColor.Red
        },
        icon = finalIcon
    )
}

enum class ArkhamAlertButtonStyle {
    DEFAULT, CANCEL, DESTRUCTIVE
}