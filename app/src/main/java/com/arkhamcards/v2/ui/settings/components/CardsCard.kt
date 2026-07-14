package com.arkhamcards.v2.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.SUPPORTED_LANGUAGES
import com.arkhamcards.v2.domain.model.meta.TabooSet
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamCheckboxButton
import com.arkhamcards.v2.ui.components.ArkhamDialog
import com.arkhamcards.v2.ui.components.ArkhamRoundedCardHeader
import com.arkhamcards.v2.ui.components.ArkhamRoundedFactionCard
import com.arkhamcards.v2.ui.components.ArkhamSquareButton
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButton
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButtonGroup
import com.arkhamcards.v2.ui.components.ArkhamTabooSetButton
import com.arkhamcards.v2.ui.components.Faction
import com.arkhamcards.v2.ui.components.iconSize
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.LocalLanguage
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale

@Composable
fun CardsCard(
    onLanguageChange: (String) -> Unit,
    collection: Collection,
    ignoreCollection: Boolean,
    navigateToCollection: () -> Unit,
    setTaboo: (Int) -> Unit,
    tabooSetId: Int,
    tabooSetsList: ImmutableList<TabooSet>,
    updateCards: (String) -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    val languageTag = LocalLanguage.current.languageTag

    ArkhamRoundedFactionCard(
        faction = Faction.Neutral,
        header = { ArkhamRoundedCardHeader(
            title = stringResource(R.string.cards),
            faction = Faction.Neutral,
        ) },
    ) {
        ArkhamSurfaceButtonGroup(modifier) {
            ArkhamLanguageSurfaceButton(languageTag, onLanguageChange)

            HorizontalDivider(color = CustomTheme.colors.divider)

            ArkhamCollectionSurfaceButton(
                collection = collection,
                ignoreCollection = ignoreCollection,
                navigateToCollection = navigateToCollection
            )

            HorizontalDivider(color = CustomTheme.colors.divider)

            ArkhamTabooSetButton(
                tabooSetId,
                tabooSetsList,
                includeCurrent = true,
                loading = loading,
                onTabooSetChange = setTaboo
            )

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
    languageTag: String,
    onLanguageChange: (String) -> Unit
) {
    val languageName = Locale.forLanguageTag(languageTag).displayLanguage
    var showLanguagePicker by remember { mutableStateOf(false) }

    ArkhamSurfaceButton(
        title = stringResource(R.string.language),
        icon = AppIcon.World,
        valueLabel = languageName,
        onClick = { showLanguagePicker = true }
    )

    if (showLanguagePicker) ArkhamDialog(
        title = stringResource(R.string.language),
        onDismiss = { showLanguagePicker = false },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.6f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item("description") {
                Text(
                    text = stringResource(R.string.note_not_all_cards_have_translations_available),
                    style = CustomTheme.typography.text,
                )
                HorizontalDivider(color = CustomTheme.colors.divider)
            }

            SUPPORTED_LANGUAGES.forEach { language ->
                item(language) {
                    val resolvedLanguage = Locale.forLanguageTag(when (language) {
                        "zh-cn" -> "zh-Hans"
                        "zh" -> "zh-Hant"
                        else -> language
                    })
                    ArkhamCheckboxButton(
                        title = resolvedLanguage.getDisplayName(resolvedLanguage),
                        isSelected = language == languageTag,
                        enabled = language != languageTag,
                        isRadio = true
                    ) {
                        showLanguagePicker = false
                        onLanguageChange(language)
                    }
                }
                item {
                    HorizontalDivider(color = CustomTheme.colors.divider)
                }
            }
        }
    }
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