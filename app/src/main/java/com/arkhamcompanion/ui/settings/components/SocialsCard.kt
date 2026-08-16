package com.arkhamcompanion.ui.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcompanion.R
import com.arkhamcompanion.domain.enums.Faction
import com.arkhamcompanion.ui.components.ArkhamIconText
import com.arkhamcompanion.ui.components.ArkhamRoundedCardHeader
import com.arkhamcompanion.ui.components.ArkhamRoundedFactionCard
import com.arkhamcompanion.ui.components.ArkhamSquareButton
import com.arkhamcompanion.ui.components.iconSize
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.theme.CustomTheme
import com.arkhamcompanion.ui.utils.openLink

@Composable
fun SocialsCard(
    languageTag: String,
) {
    val context = LocalContext.current

    ArkhamRoundedFactionCard(
        faction = Faction.Neutral,
        header = {
            ArkhamRoundedCardHeader(
                title = stringResource(R.string.social),
                faction = Faction.Neutral,
            )
        },
    ) {
        Text(
            text = stringResource(R.string.socials_text),
            style = CustomTheme.typography.text,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        ArkhamSquareButton(
            title = stringResource(R.string.discord),
            onClick = remember {
                {
                    context.openLink(
                        when (languageTag) {
                            "ru" -> "https://discord.gg/cqUudV2"
                            else -> "https://discord.gg/RpPgDQDfsN"
                        }
                    )
                }
            },
        ) { color ->
            ArkhamIconText(
                iconGlyph = AppIcon.Discord,
                color = color,
                size = iconSize(AppIcon.Discord),
            )
        }
        if (languageTag == "es") {
            ArkhamSquareButton(
                title = "Tutorial aplicación",
                onClick = remember {
                    {
                        context.openLink("https://www.youtube.com/watch?v=Vt9PCm02owU&list=PLFbghkzYxuOj4l3dF9ljqSqd_MKGibzei")
                    }
                },
            ) { color ->
                ArkhamIconText(
                    iconGlyph = AppIcon.Wild,
                    color = color,
                    size = iconSize(AppIcon.Wild)
                )
            }
        } else if (languageTag == "ru") {
            ArkhamSquareButton(
                title = stringResource(R.string.vk),
                onClick = remember {
                    {
                        context.openLink("https://vk.com/arkham_cardgame")
                    }
                },
            ) { color ->
                ArkhamIconText(
                    iconGlyph = AppIcon.Vk,
                    color = color,
                    size = iconSize(AppIcon.Vk)
                )
            }
            ArkhamSquareButton(
                title = stringResource(R.string.telegram),
                onClick = remember {
                    {
                        context.openLink("https://t.me/arkhamhorrorlcg_ru_chat")
                    }
                },
            ) { color ->
                ArkhamIconText(
                    iconGlyph = AppIcon.Telegram,
                    color = color,
                    size = iconSize(AppIcon.Telegram)
                )
            }
        }
    }
}