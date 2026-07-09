package com.arkhamcards.v2.ui.settings.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamRoundedCardHeader
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.components.ArkhamSquareButton
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButton
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButtonGroup
import com.arkhamcards.v2.ui.components.Faction
import com.arkhamcards.v2.ui.components.iconSize
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.LocalLanguage
import java.util.Locale

@Composable
fun CardsCard(
    onLanguageChange: (String) -> Unit,
    collection: Collection,
    ignoreCollection: Boolean,
    navigateToCollection: () -> Unit,
    setTaboo: (Int) -> Unit,
    tabooSetId: Int,
    updateCards: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val languageTag = LocalLanguage.current.languageTag
    val languageName = Locale.forLanguageTag(languageTag).displayLanguage

    ArkhamRoundedFactionCard(
        faction = Faction.Neutral,
        header = { ArkhamRoundedCardHeader(
            title = stringResource(R.string.cards),
            faction = Faction.Neutral,
        ) },
    ) {
        ArkhamSurfaceButtonGroup(modifier) {
            ArkhamLanguageSurfaceButton(languageName, onLanguageChange)

            HorizontalDivider(color = CustomTheme.colors.divider)

            ArkhamCollectionSurfaceButton(
                collection = collection,
                ignoreCollection = ignoreCollection,
                navigateToCollection = navigateToCollection
            )

            HorizontalDivider(color = CustomTheme.colors.divider)

            ArkhamTabooSurfaceButton(tabooSetId, setTaboo)

            //TODO:Add card pool button
            //HorizontalDivider(color = CustomTheme.colors.divider)

//        ArkhamSurfaceButton(
//            title = stringResource(R.string.card_pool),
//            icon = AppIcon.Deck,
//            valueLabel = languageName,
//            onClick = {}
//        )
        }

        ArkhamSquareButton(
            title = stringResource(R.string.check_for_card_updates),
            onClick = { updateCards(languageTag) },
            icon = { color ->
                Text(
                    text = AppIcon.Arkhamdb.glyph,
                    fontFamily = AppIcon.Arkhamdb.fontFamily,
                    color = color,
                    fontSize = iconSize(AppIcon.Arkhamdb)
                )
            }
        )

        //TODO:Add rules button
//    ArkhamSquareButton(
//        title = stringResource(R.string.check_for_card_updates),
//        onClick = { updateCards(languageTag) },
//        icon = {}
//    )
    }
}

@Composable
private fun ArkhamLanguageSurfaceButton(
    languageName: String,
    onLanguageChange: (String) -> Unit
) {
    ArkhamSurfaceButton(
        title = stringResource(R.string.language),
        icon = AppIcon.World,
        valueLabel = languageName,
        onClick = {}
    )
}

@Composable
private fun ArkhamCollectionSurfaceButton(
    collection: Collection,
    ignoreCollection: Boolean,
    navigateToCollection: () -> Unit
) {
    val collectionValue = if (ignoreCollection) stringResource(R.string.all_cycles_and_packs)
        else pluralStringResource(R.plurals.count_cycle, collection.cycles.size) + " + " +
            pluralStringResource(R.plurals.count_pack, collection.packs.size)

    ArkhamSurfaceButton(
        title = stringResource(R.string.card_collection),
        icon = AppIcon.CardOutline,
        valueLabel = collectionValue,
        onClick = navigateToCollection
    )
}

@Composable
private fun ArkhamTabooSurfaceButton(
    tabooSetId: Int,
    onTabooSetChange: (Int) -> Unit
) {
    val tabooSetName = when (tabooSetId) {
        100 -> ""
        0 -> stringResource(R.string.none)
        else -> ""
    }

    ArkhamSurfaceButton(
        title = stringResource(R.string.taboo_set),
        icon = AppIcon.Taboo,
        valueLabel = tabooSetName,
        onClick = {}
    )
}