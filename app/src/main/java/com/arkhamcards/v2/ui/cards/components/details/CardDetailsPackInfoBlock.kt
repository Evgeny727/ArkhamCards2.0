package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.domain.model.cards.CardDetailsWithPackInfo
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.appSp

@Composable
fun CardDetailsPackInfoBlock(
    cardDetailsWithPackInfo: CardDetailsWithPackInfo,
    firstPackInCollection: String?,
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(color = CustomTheme.colors.l10)

    cardDetailsWithPackInfo.cardDetails.run {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            illustrator?.let { illustrator ->
                Row(
                    modifier = Modifier.weight(0.5f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppIcon.Paintbrush.glyph,
                        fontFamily = AppIconsFont,
                        fontSize = 14.appSp(CustomTheme.typography.scaleFactor),
                        color = CustomTheme.colors.d20
                    )

                    Text(
                        text = illustrator,
                        style = CustomTheme.typography.tiny
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    encounterCode?.let {
                        val icon = PackIcon.fromPackCode(it)
                        val quantityText = if (quantity > 1) "$encounterPosition - ${encounterPosition!! + quantity - 1}"
                            else encounterPosition!!.toString()

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = encounterName!!,
                                style = CustomTheme.typography.tiny,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            Text(
                                text = icon.glyph,
                                fontFamily = icon.fontFamily,
                                fontSize = 14.appSp(CustomTheme.typography.scaleFactor),
                                color = CustomTheme.colors.darkText
                            )

                            Text(
                                text = quantityText,
                                style = CustomTheme.typography.tiny
                            )
                        }
                    }

                    cardDetailsWithPackInfo.allPacks.forEach { pack ->
                        val icon = PackIcon.fromPackCode(pack.code)
                        val firstPack = firstPackInCollection == pack.code

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pack.name,
                                style = CustomTheme.typography.tiny.copy(
                                    fontWeight = if (firstPack) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            Spacer(modifier = Modifier.width(0.dp))

                            Text(
                                text = icon.glyph,
                                fontFamily = icon.fontFamily,
                                fontSize = 14.appSp(CustomTheme.typography.scaleFactor),
                                color = CustomTheme.colors.darkText
                            )

                            Spacer(modifier = Modifier.width(0.dp))

                            Text(
                                text = pack.position.toString(),
                                style = CustomTheme.typography.tiny.copy(
                                    fontWeight = if (firstPack) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = AppIcon.CardOutline.glyph,
                                fontFamily = AppIconsFont,
                                fontSize = 14.appSp(CustomTheme.typography.scaleFactor),
                                color = CustomTheme.colors.darkText
                            )

                            Text(
                                text = "×${pack.quantity}",
                                style = CustomTheme.typography.tiny.copy(
                                    fontWeight = if (firstPack) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}