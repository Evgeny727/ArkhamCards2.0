package com.arkhamcards.v2.ui.settings.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.ui.components.ArkhamButtonColor
import com.arkhamcards.v2.ui.components.ArkhamRoundedCardHeader
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.components.ArkhamSquareButton
import com.arkhamcards.v2.ui.components.Faction
import com.arkhamcards.v2.ui.components.iconSize
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.utils.openEmail
import com.arkhamcards.v2.ui.utils.openLink

const val patreonLink = "https://patreon.com/rangerscards"

@Composable
fun SupportCard(
    navigateToAbout: () -> Unit,
    navigateToBackUp: () -> Unit,
    navigateToDiagnostics: () -> Unit,
) {
    val context = LocalContext.current

    ArkhamRoundedFactionCard(
        faction = Faction.Neutral,
        header = { ArkhamRoundedCardHeader(
            title = stringResource(R.string.support),
            faction = Faction.Neutral,
        ) },
    ) {
        ArkhamSquareButton(
            title = stringResource(R.string.patreon_button),
            onClick = remember { {
                context.openLink(patreonLink)
            } },
        ) { color ->
            Icon(
                painter = painterResource(R.drawable.patreon_logo),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        ArkhamSquareButton(
            title = stringResource(R.string.about_arkham_cards),
            onClick = navigateToAbout,
        ) { color ->
            Text(
                text = AppIcon.Logo.glyph,
                fontFamily = AppIcon.Logo.fontFamily,
                color = color,
                fontSize = iconSize(AppIcon.Logo)
            )
        }
        ArkhamSquareButton(
            title = stringResource(R.string.backup_data),
            onClick = navigateToBackUp,
        ) { color ->
            Text(
                text = AppIcon.Book.glyph,
                fontFamily = AppIcon.Book.fontFamily,
                color = color,
                fontSize = iconSize(AppIcon.Book)
            )
        }
        ArkhamSquareButton(
            title = stringResource(R.string.diagnostics),
            onClick = navigateToDiagnostics,
        ) { color ->
            Text(
                text = AppIcon.Wrench.glyph,
                fontFamily = AppIcon.Wrench.fontFamily,
                color = color,
                fontSize = iconSize(AppIcon.Wrench)
            )
        }

        ArkhamSquareButton(
            title = stringResource(R.string.contact_us),
            onClick = remember { {
                context.openEmail("arkhamcardsv2@gmail.com")
            } },
            colors = ArkhamButtonColor.Gold
        ) { color ->
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}