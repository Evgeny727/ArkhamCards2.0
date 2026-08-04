package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardDetails
import com.arkhamcards.v2.ui.components.factionColor
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.appSp
import com.arkhamcards.v2.ui.utils.iconize

@Composable
fun CardDetailsHeader(
    cardDetails: CardDetails,
    modifier: Modifier = Modifier,
) {
    val cardFaction = if (cardDetails.faction2 != null) Faction.Dual else cardDetails.faction

    val density = LocalDensity.current
    val minRowHeight = with(density) {
        (26 + 21).appSp(CustomTheme.typography.scaleFactor).toDp() + 16.dp
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        color = factionColor(cardFaction).background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minRowHeight)
                .paint(
                    painter = painterResource(when (cardFaction) {
                        Faction.Guardian -> R.drawable.guardian
                        Faction.Seeker -> R.drawable.seeker
                        Faction.Rogue -> R.drawable.rogue
                        Faction.Mystic -> R.drawable.mystic
                        Faction.Survivor -> R.drawable.survivor
                        Faction.Neutral -> R.drawable.neutral
                        Faction.Dual -> R.drawable.multiclass
                        else -> R.drawable.mythos
                    }),
                    alpha = 0.2f,
                    contentScale = ContentScale.Crop
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val showCost = cardDetails.type == CardType.Asset ||
                        cardDetails.type == CardType.Event || cardDetails.type == CardType.Skill
                val iconScaleFactor = iconScaleFactor(CustomTheme.typography.scaleFactor)

                //cost
                if (showCost) {
                    Box(contentAlignment = Alignment.Center) {
                        CardCostIcon(
                            xp = cardDetails.xp,
                            type = cardDetails.type,
                            subType = cardDetails.subType,
                            faction = cardDetails.faction,
                            faction2 = cardDetails.faction2,
                            factionColor = CustomTheme.colors.faction.neutral,
                            cost = cardDetails.cost,
                            inverted = true,
                            iconScaleFactor = iconScaleFactor
                        )
                    }
                }

                //name block
                CardDetailsNameRow(
                    parallel = cardDetails.parallel,
                    isUnique = cardDetails.isUnique,
                    name = cardDetails.name,
                    packCode = cardDetails.reprintPackCode ?: cardDetails.packCode,
                    packPosition = cardDetails.packPosition,
                    subname = cardDetails.subname
                )

                //faction icons
                CardDetailsFactionIcons(
                    isWithCost = showCost,
                    subType = cardDetails.subType,
                    faction = cardDetails.faction,
                    faction2 = cardDetails.faction2,
                    faction3 = cardDetails.faction3,
                    encounterCode = cardDetails.encounterCode,
                    iconScaleFactor = iconScaleFactor
                )
            }
        }
    }
}

@Composable
fun CardDetailsFactionIcons(
    isWithCost: Boolean,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    faction3: Faction?,
    encounterCode: String?,
    iconScaleFactor: Float
) {
    if (subType == CardSubType.BasicWeakness || subType == CardSubType.Weakness) {
        Text(
            text = AppIcon.Weakness.glyph,
            fontFamily = AppIconsFont,
            fontSize = 32.appSp(iconScaleFactor),
            color = Color.White,
        )
    } else if (encounterCode != null && !isWithCost) {
        EncounterIcon(
            iconCode = encounterCode,
            iconSize = 32.appSp(iconScaleFactor),
            iconColor = Color.White,
        )
    } else {
        val factionIcon = factionIcon(faction)
        Text(
            text = factionIcon.glyph,
            fontFamily = factionIcon.fontFamily,
            fontSize = 32.appSp(iconScaleFactor),
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        faction2?.let {
            val faction2Icon = factionIcon(faction2)
            Text(
                text = faction2Icon.glyph,
                fontFamily = faction2Icon.fontFamily,
                fontSize = 32.appSp(iconScaleFactor),
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        faction3?.let {
            val faction3Icon = factionIcon(faction3)
            Text(
                text = faction3Icon.glyph,
                fontFamily = faction3Icon.fontFamily,
                fontSize = 32.appSp(iconScaleFactor),
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun RowScope.CardDetailsNameRow(
    parallel: Boolean,
    isUnique: Boolean,
    name: String,
    packCode: String,
    packPosition: Int,
    subname: String?,
) {
    Column(modifier = Modifier.weight(1f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val iconSize = 16.appSp(CustomTheme.typography.scaleFactor)
            val nameString = buildAnnotatedString {
                if (parallel) {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = AppIconsFont,
                            fontSize = iconSize
                        )
                    ) {
                        append(AppIcon.Parallel.glyph)
                    }
                }
                if (isUnique) withStyle(
                    style = SpanStyle(fontSize = iconSize)
                ) {
                    append("$UNIQUE_SYMBOL ")
                }

                append(name.iconize(iconSize = 16.sp, color = Color.White))

                if (packPosition != 1000) {
                    val packIcon = PackIcon.fromPackCode(packCode)

                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            fontFamily = packIcon.fontFamily,
                            fontSize = iconSize,
                        )
                    ) {
                        append(packIcon.glyph)
                    }

                    append('\u00A0')

                    withStyle(
                        style = SpanStyle(fontSize = iconSize)
                    ) {
                        append(packPosition.toString())
                    }
                }
            }

            Text(
                text = nameString,
                style = CustomTheme.typography.cardName,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

        subname?.let {
            Text(
                text = subname.iconize(iconSize = 12.sp, color = Color.White),
                style = CustomTheme.typography.cardTraits,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
            )
        }
    }
}