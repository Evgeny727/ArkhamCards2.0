package com.arkhamcards.v2.ui.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.ui.components.ArkhamRoundedCardHeader
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.components.ArkhamSquareButton
import com.arkhamcards.v2.ui.components.Faction
import com.arkhamcards.v2.ui.components.iconSize
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.openLink

@Composable
fun SocialsCard(
    languageTag: String,
) {
    val context = LocalContext.current

    ArkhamRoundedFactionCard(
        faction = Faction.Neutral,
        header = { ArkhamRoundedCardHeader(
            title = stringResource(R.string.social),
            faction = Faction.Neutral,
        ) },
    ) {
        if (languageTag == "ru") {
            Text(
                text = "Общайтесь с другими поклонниками карточного «Ужаса Аркхэма» в русскоязычных сообществах:",
                style = CustomTheme.typography.text,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            ArkhamSquareButton(
                title = stringResource(R.string.discord),
                onClick = remember { {
                    context.openLink("https://discord.gg/cqUudV2")
                } },
            ) { color ->
                Text(
                    text = AppIcon.Discord.glyph,
                    fontFamily = AppIcon.Discord.fontFamily,
                    color = color,
                    fontSize = iconSize(AppIcon.Discord)
                )
            }
            ArkhamSquareButton(
                title = stringResource(R.string.vk),
                onClick = remember { {
                    context.openLink("https://vk.com/arkham_cardgame")
                } },
            ) { color ->
                Text(
                    text = AppIcon.Vk.glyph,
                    fontFamily = AppIcon.Vk.fontFamily,
                    color = color,
                    fontSize = iconSize(AppIcon.Vk)
                )
            }
            ArkhamSquareButton(
                title = stringResource(R.string.telegram),
                onClick = remember { {
                    context.openLink("https://t.me/arkhamhorrorlcg_ru_chat")
                } },
            ) { color ->
                Text(
                    text = AppIcon.Telegram.glyph,
                    fontFamily = AppIcon.Telegram.fontFamily,
                    color = color,
                    fontSize = iconSize(AppIcon.Telegram)
                )
            }
        } else if (languageTag == "es") {
            ArkhamSquareButton(
                title = "Tutorial aplicación",
                onClick = remember { {
                    context.openLink("https://www.youtube.com/watch?v=Vt9PCm02owU&list=PLFbghkzYxuOj4l3dF9ljqSqd_MKGibzei")
                } },
            ) { color ->
                Text(
                    text = AppIcon.Wild.glyph,
                    fontFamily = AppIcon.Wild.fontFamily,
                    color = color,
                    fontSize = iconSize(AppIcon.Wild)
                )
            }
        }
    }
}