package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.arkhamcards.v2.domain.enums.CardBackType
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.ui.cards.components.factionIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun CardDetailsClickableThumbnail(
    thumbnailUrl: String?,
    imageUrl: String?,
    backImageUrl: String?,
    taboSetId: String?,
    type: CardType,
    backType: CardBackType,
    encounterCode: String?,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    modifier: Modifier = Modifier
) {
    var hidePlaceholder by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        if (!hidePlaceholder) {
            Box(
                modifier = modifier
                    .size(108.dp)
                    .clip(CustomTheme.shapes.medium)
                    .background(CustomTheme.colors.divider)
                    .border(1.dp, CustomTheme.colors.darkText, CustomTheme.shapes.medium)
                    .clickable(enabled = imageUrl != null) {
                        /*TODO:open full sized image in dialog*/
                    },
                contentAlignment = Alignment.Center
            ) {
                val icon = if (encounterCode != null) {
                    PackIcon.fromPackCode(encounterCode)
                } else {
                    factionIcon(faction, faction2, subType)
                }

                Text(
                    text = icon.glyph,
                    fontFamily = icon.fontFamily,
                    fontSize = 48.sp,
                    color = CustomTheme.colors.lightText
                )
            }
        }

        if (thumbnailUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .build(),
                modifier = modifier
                    .size(108.dp)
                    .clip(CustomTheme.shapes.medium)
                    .clickable(enabled = imageUrl != null) {
                        /*TODO:open full sized image in dialog*/
                    },
                onSuccess = { hidePlaceholder = true },
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}