package com.arkhamcards.v2.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButton
import com.arkhamcards.v2.ui.components.ArkhamSurfaceButtonGroup
import com.arkhamcards.v2.ui.settings.components.CardsCard
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLanguageChange: (String) -> Unit,
    navigateToCollection: () -> Unit,
    updateCards: (String) -> Unit,
    emitError: (Throwable) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val includeEnglish by viewModel.isIncludeEnglishSearchResultsState.collectAsState()
    val collection by viewModel.collectionState.collectAsState()
    val ignoreCollection by viewModel.ignoreCollectionState.collectAsState()
    val tabooSetId by viewModel.tabooSetIdState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errors.collect {
            emitError(it.exception)
        }
    }

    SettingsScreenContent(
        onLanguageChange = onLanguageChange,
        onThemeChange = viewModel::selectTheme,
        onScaleFactorChange = viewModel::setScaleFactor,
        onEnglishSearchResultsChange = viewModel::setEnglishSearchResults,
        isIncludeEnglishSearchResults = includeEnglish,
        navigateToCollection = navigateToCollection,
        collection = collection,
        ignoreCollection = ignoreCollection,
        onTabooSetChange = viewModel::setTaboo,
        tabooSetId = tabooSetId,
        updateCards = updateCards,
        paddingValues = innerPadding,
        modifier = modifier
    )
}

@Composable
private fun SettingsScreenContent(
    onLanguageChange: (String) -> Unit,
    onThemeChange: (Int) -> Unit,
    onScaleFactorChange: (Float) -> Unit,
    onEnglishSearchResultsChange: (Boolean) -> Unit,
    isIncludeEnglishSearchResults: Boolean,
    navigateToCollection: () -> Unit,
    collection: Collection,
    ignoreCollection: Boolean,
    onTabooSetChange: (Int) -> Unit,
    tabooSetId: Int,
    updateCards: (String) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.applyScaffoldPaddings(paddingValues)
            .fillMaxSize()
            .background(CustomTheme.colors.l10),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //TODO: add account card
//        item("account_card") {
//            Text(text = "Settings", style = CustomTheme.typography.header)
//        }
        //TODO: add arkham.build account card
//        item("arkhambuild_account_card") {
//            Text(text = "Settings", style = CustomTheme.typography.header)
//        }
        item("cards_card") {
            CardsCard(
                onLanguageChange,
                collection,
                ignoreCollection,
                navigateToCollection,
                setTaboo = onTabooSetChange,
                tabooSetId,
                updateCards,
            )
        }
        item("settings_card") {

        }
        //TODO: add narration card
//        item("narration_card") {
//            Text(text = "Settings", style = CustomTheme.typography.header)
//        }
        item("socials_card") {

        }
        item("support_card") {

        }
    }
}