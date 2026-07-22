package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.domain.model.cards.CardListItem
import com.arkhamcards.v2.ui.components.factionColor
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.FactionColors
import com.arkhamcards.v2.ui.utils.appSp
import com.arkhamcards.v2.ui.utils.iconize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

const val UNIQUE_SYMBOL = "✷"

@Composable
fun CardListItem(
    cardListItem: CardListItem,
    rowHeight: Dp,
    modifier: Modifier = Modifier,
    invalid: Boolean = false,
    onClick: () -> Unit,
) {
    val factionColor = factionColor(if (cardListItem.faction2 != null) Faction.Dual
        else cardListItem.faction)

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(rowHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cardListItem.thumbnailUrl?.let { thumbnail ->
                    CardListItemThumbnail(
                        thumbnailUrl = thumbnail,
                        factionColor = factionColor,
                    )
                }

                with(cardListItem) {
                    CardIcon(xp, type, subType, faction, faction2, factionColor, realCost, packCode, encounterCode)
                }

                CardListItemName(cardListItem, factionColor, invalid)
            }

            HorizontalDivider(color = CustomTheme.colors.divider)
        }
    }
}

@Composable
fun CardListItemThumbnail(
    thumbnailUrl: String,
    factionColor: FactionColors,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnailUrl)
            .build(),
        modifier = modifier
            .fillMaxHeight(0.8f)
            .aspectRatio(1f)
            .clip(CustomTheme.shapes.small)
            .border(1.dp, factionColor.text, CustomTheme.shapes.small),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun RowScope.CardListItemName(
    cardListItem: CardListItem,
    factionColor: FactionColors,
    invalid: Boolean
) {
    Column(modifier = Modifier.weight(1f)) {
        CardListItemNameRow(
            name = cardListItem.name,
            isUnique = cardListItem.isUnique,
            packCode = cardListItem.packCode,
            packPosition = cardListItem.packPosition,
            reprintPackCode = cardListItem.reprintPackCode,
            factionColor = factionColor,
            invalid = invalid
        )
        CardListItemSubname(cardListItem)
    }
}

@Composable
fun CardListItemNameRow(
    name: String,
    isUnique: Boolean,
    packCode: String,
    packPosition: Int,
    reprintPackCode: String?,
    factionColor: FactionColors,
    invalid: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isUnique) Text(
            text = UNIQUE_SYMBOL,
            style = CustomTheme.typography.run { cardTraits + regular },
            color = factionColor.text,
            textDecoration = if (invalid) TextDecoration.LineThrough else null
        )

        Text(
            text = name.iconize(iconSize = 16.sp, color = CustomTheme.colors.darkText),
            style = CustomTheme.typography.cardName,
            color = factionColor.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textDecoration = if (invalid) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f, fill = false)
        )

        //Exclude pack info for random basic weakness
        if (packPosition != 1000) Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val packIcon = PackIcon.fromPackCode(reprintPackCode ?: packCode)
            Text(
                text = packIcon.glyph,
                fontFamily = packIcon.fontFamily,
                style = CustomTheme.typography.run { cardTraits + regular },
                textDecoration = if (invalid) TextDecoration.LineThrough else null,
            )
            Text(
                text = packPosition.toString(),
                style = CustomTheme.typography.run { cardTraits + regular },
                textDecoration = if (invalid) TextDecoration.LineThrough else null,
            )
        }
    }
}

@Composable
fun CardListItemSubname(cardListItem: CardListItem) {
    val subname = cardListItem.subname ?:
        if (cardListItem.type == CardType.Act || cardListItem.type == CardType.Agenda
            || cardListItem.type == CardType.Scenario) {
                cardListItem.typeName + if (cardListItem.stage != null) " ${cardListItem.stage}" else ""
        } else {
            ""
        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (cardListItem.faction != Faction.Mythos && cardListItem.type != CardType.Investigator
            && cardListItem.type != CardType.Skill) {
                val factionIcon = factionIcon(cardListItem.faction)
                Text(
                    text = factionIcon.glyph,
                    fontFamily = factionIcon.fontFamily,
                    style = CustomTheme.typography.run { cardTraits + regular },
                    color = factionColor(cardListItem.faction).text
                )
                cardListItem.faction2?.let { faction ->
                    val factionIcon = factionIcon(faction)
                    Text(
                        text = factionIcon.glyph,
                        fontFamily = factionIcon.fontFamily,
                        style = CustomTheme.typography.run { cardTraits + regular },
                        color = factionColor(faction).text
                    )
                }
                cardListItem.faction3?.let { faction ->
                    val factionIcon = factionIcon(faction)
                    Text(
                        text = factionIcon.glyph,
                        fontFamily = factionIcon.fontFamily,
                        style = CustomTheme.typography.run { cardTraits + regular },
                        color = factionColor(faction).text
                    )
                }
        }

        if (cardListItem.parallel) Text(
            text = AppIcon.Parallel1.glyph,
            fontFamily = AppIconsFont,
            style = CustomTheme.typography.run { cardTraits + regular }
        )

        if (cardListItem.type != CardType.Investigator) {
            val skills = with(cardListItem) {
                buildSkillTextRow(skillWillpower, skillIntellect, skillCombat, skillAgility, skillWild)
            }
            if (skills.isNotEmpty()) Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                skills.forEach { skill ->
                    Text(
                        text = skill,
                        fontFamily = AppIconsFont,
                        style = CustomTheme.typography.run { cardTraits + regular },
                        color = CustomTheme.colors.lightText,
                    )
                }
            }
        }

        cardListItem.tabooSetId?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = AppIcon.Tablet.glyph,
                    fontFamily = AppIconsFont,
                    fontSize = 14.appSp(CustomTheme.typography.scaleFactor),
                    color = CustomTheme.colors.taboo,
                )
                cardListItem.tabooXp?.let { xp ->
                    val taboo = buildTabooXpRow(xp)
                    if (taboo.isNotEmpty()) taboo.forEach { symbol ->
                        Text(
                            text = symbol,
                            style = CustomTheme.typography.small,
                            color = CustomTheme.colors.taboo,
                        )
                    }
                }
            }
        }

        if (subname.isNotBlank()) Text(
            text = subname.iconize(iconSize = 12.sp, color = CustomTheme.colors.d10),
            style = CustomTheme.typography.cardTraits,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Stable
private fun buildSkillTextRow(
    willpower: Int?,
    intellect: Int?,
    combat: Int?,
    agility: Int?,
    wild: Int?
): ImmutableList<String> {
    val skills = mutableListOf<String>()
    repeat(willpower ?: 0) {
        skills.add(AppIcon.Willpower.glyph)
    }
    repeat(intellect ?: 0) {
        skills.add(AppIcon.Intellect.glyph)
    }
    repeat(combat ?: 0) {
        skills.add(AppIcon.Combat.glyph)
    }
    repeat(agility ?: 0) {
        skills.add(AppIcon.Agility.glyph)
    }
    repeat(wild ?: 0) {
        skills.add(AppIcon.Wild.glyph)
    }
    return skills.toImmutableList()
}

@Stable
private fun buildTabooXpRow(xp: Int): ImmutableList<String> {
    val taboo = mutableListOf<String>()
    repeat(xp.absoluteValue) {
        if (xp > 0) taboo.add("•")
        else taboo.add("-")
    }
    return taboo.toImmutableList()
}