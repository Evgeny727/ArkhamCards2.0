package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.enums.CardBackType
import com.arkhamcards.v2.domain.enums.CardSubType
import com.arkhamcards.v2.domain.enums.CardType
import com.arkhamcards.v2.domain.enums.Faction
import com.arkhamcards.v2.ui.cards.components.factionIcon
import com.arkhamcards.v2.ui.components.ArkhamButtonColor
import com.arkhamcards.v2.ui.components.ArkhamDialog
import com.arkhamcards.v2.ui.components.ArkhamSquareButton
import com.arkhamcards.v2.ui.components.iconSize
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.icons.PackIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun CardDetailsClickableThumbnail(
    thumbnailUrl: String?,
    imageUrl: String?,
    backImageUrl: String?,
    taboSetId: String?,
    backTaboSetId: String?,
    code: String,
    backCode: String?,
    type: CardType,
    backCardType: CardType?,
    backType: CardBackType,
    encounterCode: String?,
    subType: CardSubType?,
    faction: Faction,
    faction2: Faction?,
    modifier: Modifier = Modifier,
    isBackFirst: Boolean = false,
) {
    var hidePlaceholder by remember { mutableStateOf(false) }
    var showFullImageDialog by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        if (!hidePlaceholder) {
            Box(
                modifier = modifier
                    .size(108.dp)
                    .clip(CustomTheme.shapes.medium)
                    .background(CustomTheme.colors.divider)
                    .border(1.dp, CustomTheme.colors.darkText, CustomTheme.shapes.medium)
                    .clickable(enabled = imageUrl != null) { showFullImageDialog = true },
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
                    .clickable(enabled = imageUrl != null) { showFullImageDialog = true },
                onSuccess = { hidePlaceholder = true },
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        } else if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                modifier = modifier
                    .size(108.dp)
                    .clip(CustomTheme.shapes.medium)
                    .clickable { showFullImageDialog = true },
                onSuccess = { hidePlaceholder = true },
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }

    if (showFullImageDialog) {
//        val backImageUrl = if (backCode != null && official) {
//            ARKHAM_BUILD_BASE_IMAGE_URL + "optimized/${backCode}${
//                if (backTaboSetId != null) "-$backTaboSetId" else ""
//            }.webp"
//        } else backImageUrl

        CardDetailsFullImageDialog(
            code = code,
            backCode = backCode,
            cardType = type,
            backCardType = backCardType,
            taboSetId = taboSetId,
            backTaboSetId = backTaboSetId,
            imageUrl = imageUrl,
            backImageUrl = backImageUrl,
            isDoubleSided = backCode != null || backType == CardBackType.Card,
            isBackFirst = isBackFirst,
            onDismiss = { showFullImageDialog = false },
        )
    }
}

@Composable
private fun CardDetailsFullImageDialog(
    code: String,
    backCode: String?,
    cardType: CardType,
    backCardType: CardType?,
    taboSetId: String?,
    backTaboSetId: String?,
    imageUrl: String?,
    backImageUrl: String?,
    isDoubleSided: Boolean,
    isBackFirst: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hidePlaceholder by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var showWithoutTaboo by remember { mutableStateOf(false) }
    var showBack by remember { mutableStateOf(isBackFirst) }

    val isFrontSideways = isSideways(cardType, code)
    val isBackSideways = when {
        isDoubleSided && backCode == null -> isFrontSideways
        isDoubleSided -> isSideways(backCardType, backCode)
        else -> false
    }
    val sideWayWidth = 310.dp
    val sideWayHeight = 220.dp

    val backTabooId = if (backCode != null) backTaboSetId else taboSetId
    var imageUrl by remember { mutableStateOf(imageUrl) }
    var backImageUrl by remember { mutableStateOf(backImageUrl) }


    ArkhamDialog(
        title = stringResource(R.string.card_scan),
        modifier = modifier,
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = modifier.size(310.dp).align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            if (!hidePlaceholder) {
                Box(
                    modifier = Modifier
                        .size(310.dp)
                        .clip(CustomTheme.shapes.medium)
                        .background(CustomTheme.colors.divider)
                        .border(1.dp, CustomTheme.colors.darkText, CustomTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppIcon.Logo.glyph,
                        fontFamily = AppIconsFont,
                        fontSize = 72.sp,
                        color = CustomTheme.colors.lightText
                    )
                }
            }

            if (isLoading) CircularProgressIndicator(
                color = CustomTheme.colors.darkText,
                modifier = Modifier.size(56.dp).align(Alignment.Center)
            )

            if (showBack) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(backImageUrl)
                        .build(),
                    modifier = Modifier
                        .width(if (isBackSideways) sideWayWidth else sideWayHeight)
                        .height(if (isBackSideways) sideWayHeight else sideWayWidth)
                        .clip(CustomTheme.shapes.large),
                    onSuccess = { hidePlaceholder = true; isLoading = false },
                    onError = { hidePlaceholder = false; isLoading = false },
                    onLoading = { isLoading = true },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build(),
                    modifier = Modifier
                        .width(if (isFrontSideways) sideWayWidth else sideWayHeight)
                        .height(if (isFrontSideways) sideWayHeight else sideWayWidth)
                        .clip(CustomTheme.shapes.large),
                    onSuccess = { hidePlaceholder = true; isLoading = false },
                    onError = { hidePlaceholder = false; isLoading = false },
                    onLoading = { isLoading = true },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }

        ArkhamSquareButton(
            title = stringResource(R.string.flip_card),
            onClick = { showBack = !showBack },
            colors = ArkhamButtonColor.Default,
            icon = { color ->
                Text(
                    text = AppIcon.FlipCard.glyph,
                    fontFamily = AppIcon.FlipCard.fontFamily,
                    fontSize = iconSize(AppIcon.FlipCard),
                    color = color
                )
            }
        )

        if (taboSetId != null) {
            ArkhamSquareButton(
                title = stringResource(if (showWithoutTaboo) R.string.card_scan_with_taboo
                else R.string.card_scan_without_taboo),
                onClick = {
                    val newShowWithoutTaboo = !showWithoutTaboo
                    showWithoutTaboo = newShowWithoutTaboo

                    if (newShowWithoutTaboo) {
                        imageUrl = imageUrl.removeTaboo(taboSetId)
                        backImageUrl = backImageUrl.removeTaboo(backTabooId)
                    } else {
                        imageUrl = imageUrl.applyTaboo(taboSetId)
                        backImageUrl = backImageUrl.applyTaboo(backTabooId)
                    }
                },
                colors = ArkhamButtonColor.Default,
                icon = { color ->
                    Text(
                        text = AppIcon.Taboo.glyph,
                        fontFamily = AppIcon.Taboo.fontFamily,
                        fontSize = iconSize(AppIcon.Taboo),
                        color = color
                    )
                }
            )
        }

    }
}

private fun String?.applyTaboo(tabooSetId: String?): String? {
    if (this == null) return null
    if (tabooSetId == null) return this

    return replaceFirst(".webp", "-$tabooSetId.webp")
}

private fun String?.removeTaboo(tabooSetId: String?): String? {
    if (this == null) return null
    if (tabooSetId == null) return this

    return replaceFirst("-$tabooSetId", "")
}

private fun isSideways(cardType: CardType?, code: String?): Boolean {
    val result = cardType in SIDEWAYS_TYPE_CODES
    return if (code in ORIENTATION_CHANGED_CARDS) !result else result
}

private val SIDEWAYS_TYPE_CODES = arrayOf(CardType.Act, CardType.Agenda, CardType.Investigator)
private val ORIENTATION_CHANGED_CARDS = arrayOf("85037", "85038")