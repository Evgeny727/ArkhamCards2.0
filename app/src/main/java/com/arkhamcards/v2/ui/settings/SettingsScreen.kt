package com.arkhamcards.v2.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.ui.settings.components.CardsCard
import com.arkhamcards.v2.ui.settings.components.SettingsCard
import com.arkhamcards.v2.ui.settings.components.SocialsCard
import com.arkhamcards.v2.ui.settings.components.SupportCard
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.LocalLanguage
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings

@Composable
fun SettingsScreen(
    theme: Int,
    viewModel: SettingsViewModel,
    onLanguageChange: (String) -> Unit,
    navigateToCollection: () -> Unit,
    updateCards: (String) -> Unit,
    navigateToAbout: () -> Unit,
    navigateToBackup: () -> Unit,
    navigateToDiagnostics: () -> Unit,
    emitError: (Throwable) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val settingsUiState by viewModel.settingsUiState.collectAsState()
    val showFanmadeCards by viewModel.showFanmadeCardsState.collectAsState()
    val includeEnglish by viewModel.isIncludeEnglishSearchResultsState.collectAsState()
    val collection by viewModel.collectionState.collectAsState()
    val ignoreCollection by viewModel.ignoreCollectionState.collectAsState()
    val tabooSetId by viewModel.tabooSetIdState.collectAsState()
    val tabooSetsList by viewModel.tabooSetsListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errors.collect {
            emitError(it.exception)
        }
    }

    val languageTag = LocalLanguage.current.languageTag

    LazyColumn(
        modifier = modifier.applyScaffoldPaddings(innerPadding)
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
                onLanguageChange = onLanguageChange,
                collection = collection,
                ignoreCollection = ignoreCollection,
                navigateToCollection = navigateToCollection,
                setTaboo = viewModel::setTaboo,
                tabooSetId = tabooSetId,
                tabooSetsList = tabooSetsList,
                updateCards = updateCards,
                loading = settingsUiState is SettingsUiState.Loading
            )
        }

        item("settings_card") {
            SettingsCard(
                themeInt = theme,
                onThemeChange = viewModel::selectTheme,
                scaleFactor = CustomTheme.typography.scaleFactor,
                onScaleChange = viewModel::setScaleFactor,
                showFanmadeCards = showFanmadeCards,
                onFanmadeCardsChange = viewModel::setFanmadeCards,
                includeEnglishSearchResults = includeEnglish,
                onIncludeEnglishResultsChange = viewModel::setEnglishSearchResults,
                isLoading = settingsUiState is SettingsUiState.Loading,
            )
        }

        //TODO: add narration card
//        item("narration_card") {
//            Text(text = "Settings", style = CustomTheme.typography.header)
//        }

        if (languageTag == "ru" || languageTag == "es") item("socials_card") {
            SocialsCard(languageTag)
        }

        item("support_card") {
            SupportCard(
                navigateToAbout = navigateToAbout,
                navigateToBackUp = navigateToBackup,
                navigateToDiagnostics = navigateToDiagnostics,
            )
        }
    }
}