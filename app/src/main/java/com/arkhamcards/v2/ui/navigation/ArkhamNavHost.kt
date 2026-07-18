package com.arkhamcards.v2.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.arkhamcards.v2.AppViewModel
import com.arkhamcards.v2.CardsCacheState
import com.arkhamcards.v2.CardsSyncState
import com.arkhamcards.v2.R
import com.arkhamcards.v2.ui.campaigns.Campaigns
import com.arkhamcards.v2.ui.cards.Cards
import com.arkhamcards.v2.ui.cards.CardsScreen
import com.arkhamcards.v2.ui.cards.CardsViewModel
import com.arkhamcards.v2.ui.components.ArkhamAlertButton
import com.arkhamcards.v2.ui.components.ArkhamAlertButtonStyle
import com.arkhamcards.v2.ui.components.ArkhamAlertDialog
import com.arkhamcards.v2.ui.components.ArkhamSwitch
import com.arkhamcards.v2.ui.decks.Decks
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.settings.Settings
import com.arkhamcards.v2.ui.settings.SettingsAbout
import com.arkhamcards.v2.ui.settings.SettingsBackup
import com.arkhamcards.v2.ui.settings.SettingsDiagnostics
import com.arkhamcards.v2.ui.settings.SettingsScreen
import com.arkhamcards.v2.ui.settings.SettingsViewModel
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.theme.LocalLanguage

@Composable
fun ArkhamNavHost(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val cardsState by viewModel.cardsSyncState.collectAsState()
    val cardsCacheState by viewModel.cardsCacheState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val activity = LocalActivity.current
    BackHandler {
        if (!navController.navigateUp()) activity?.finish()
    }

    //TopAppBar values
    var title by rememberSaveable { mutableStateOf("") }
    var subtitle by rememberSaveable { mutableStateOf<String?>(null) }
    val baseColor = CustomTheme.colors.background
    val baseContentColor = CustomTheme.colors.d30
    var color by remember { mutableStateOf(baseColor) }
    var contentColor by remember { mutableStateOf(baseContentColor) }
    var rightActions: @Composable (RowScope.(Color) -> Unit)? by remember { mutableStateOf(null) }
    var leftAction: @Composable ((Color) -> Unit)? by remember { mutableStateOf(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        containerColor = CustomTheme.colors.background,
        topBar = {
            ArkhamTopAppBar(
                title = title,
                subtitle = subtitle,
                color = color,
                contentColor = contentColor,
                leftAction = leftAction,
                rightActions = rightActions,
            )
        },
        bottomBar = {
            ArkhamNavigationBar(
                navController = navController,
                currentDestination = currentDestination
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(
                data,
                containerColor = CustomTheme.colors.d30,
                contentColor = CustomTheme.colors.l30
            )
        } },
    ) { innerPadding ->
        val languageTag = LocalLanguage.current.languageTag
        val resources = LocalResources.current
        LaunchedEffect(Unit) {
            viewModel.checkIfCardsReady(languageTag)
            viewModel.events.collect { error ->
                val message = when (error.exception) {
                    else -> error.exception.localizedMessage ?:
                        resources.getString(R.string.unknown_error)
                }
                snackbarHostState.showSnackbar(message)
            }
        }
        if (cardsState is CardsSyncState.UpdateAvailable) ArkhamAlertDialog(
            title = stringResource(R.string.new_cards_available),
            description = stringResource(R.string.these_cards_might_have_been_updated),
            onDismiss = viewModel::cancelCardsUpdate,
        ) {
            ArkhamAlertButton(
                text = stringResource(R.string.not_now),
                style = ArkhamAlertButtonStyle.CANCEL,
                loading = cardsCacheState is CardsCacheState.Loading,
                onClick = viewModel::cancelCardsUpdate
            )
            ArkhamAlertButton(
                text = stringResource(R.string.download_cards),
                loading = cardsCacheState is CardsCacheState.Loading,
            ) { viewModel.confirmCardsUpdate(languageTag) }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = BottomBarItem.Cards,
                enterTransition = {
                    if (initialState.destination.parent == targetState.destination.parent) {
                        fadeIn(
                            animationSpec = tween(300, easing = LinearEasing)
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    } else {
                        EnterTransition.None
                    }
                },
                exitTransition = {
                    if (initialState.destination.parent == targetState.destination.parent) {
                        fadeOut(
                            animationSpec = tween(400, easing = LinearEasing)
                        ) + slideOutOfContainer(
                            animationSpec = tween(400, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
                    } else {
                        ExitTransition.None
                    }
                }
            ) {
                navigation<BottomBarItem.Settings>(
                    startDestination = BottomBarItem.Settings.startDestination
                ) {
                    composable<Settings> {
                        val settingsViewModel = hiltViewModel<SettingsViewModel>()
                        val theme by viewModel.themeState.collectAsState()

                        SettingsScreen(
                            theme = theme ?: 2,
                            viewModel = settingsViewModel,
                            onLanguageChange = viewModel::updateLocale,
                            updateCards = viewModel::updateCardsIfAvailable,
                            navigateToCollection = {},
                            navigateToAbout = { navController.navigateSingleTop(SettingsAbout) },
                            navigateToBackup = { navController.navigateSingleTop(SettingsBackup) },
                            navigateToDiagnostics = { navController.navigateSingleTop(SettingsDiagnostics) },
                            emitError = viewModel::emitError,
                            innerPadding = innerPadding
                        )

                        title = stringResource(BottomBarItem.Settings.label)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = null
                    }
                    composable<SettingsAbout> {

                        //TODO: add about screen

                        title = stringResource(R.string.about_arkham_cards)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = { color ->
                            ArkhamAppBarAction(
                                contentColor = color,
                                onClick = navController::navigateUp,
                                iconGlyph = AppIcon.ArrowBack,
                            )
                        }
                    }
                    composable<SettingsBackup> {

                        //TODO: add backup screen

                        title = stringResource(R.string.backup_data)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = { color ->
                            ArkhamAppBarAction(
                                contentColor = color,
                                onClick = navController::navigateUp,
                                iconGlyph = AppIcon.ArrowBack,
                            )
                        }
                    }
                    composable<SettingsDiagnostics> {

                        //TODO: add diagnostics screen

                        title = stringResource(R.string.diagnostics)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = { color ->
                            ArkhamAppBarAction(
                                contentColor = color,
                                onClick = navController::navigateUp,
                                iconGlyph = AppIcon.ArrowBack,
                            )
                        }
                    }
                }
                navigation<BottomBarItem.Cards>(
                    startDestination = BottomBarItem.Cards.startDestination
                ) {
                    composable<Cards> {

                        val cardsViewModel = hiltViewModel<CardsViewModel>()
                        val spoilerState by cardsViewModel.spoilerState.collectAsState()

                        CardsScreen(
                            viewModel = cardsViewModel,
                            emitError = viewModel::emitError,
                            innerPadding = innerPadding
                        )

                        title = stringResource(if (spoilerState) R.string.encounter_cards
                            else R.string.player_cards)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = {
                            ArkhamAppBarAction(
                                contentColor = CustomTheme.colors.m,
                                onClick = {  },
                                iconGlyph = AppIcon.Filter,
                            )
                            ArkhamAppBarAction(
                                contentColor = CustomTheme.colors.m,
                                onClick = {  },
                                iconGlyph = AppIcon.Sort,
                            )
                        }
                        leftAction = {
                            ArkhamSwitch(
                                value = spoilerState,
                                onValueChange = cardsViewModel::toggleSpoiler
                            )
                        }
                    }
                }
                navigation<BottomBarItem.Decks>(
                    startDestination = BottomBarItem.Decks.startDestination
                ) {
                    composable<Decks> {

                        title = stringResource(BottomBarItem.Decks.label)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = null
                    }
                }
                navigation<BottomBarItem.Campaigns>(
                    startDestination = BottomBarItem.Campaigns.startDestination
                ) {
                    composable<Campaigns> {

                        title = stringResource(BottomBarItem.Campaigns.label)
                        subtitle = null
                        color = baseColor
                        contentColor = baseContentColor
                        rightActions = null
                        leftAction = null
                    }
                }
            }
            if (cardsState is CardsSyncState.Loading) {
                CardsDownloadingProgressIndicator((cardsState as CardsSyncState.Loading).progress)
            }
        }
    }
}

@Composable
fun CardsDownloadingProgressIndicator(progress: Float) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CustomTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.loading_latest_cards),
                color = CustomTheme.colors.d30,
                style = CustomTheme.typography.text
            )
            Spacer(modifier = Modifier.height(8.dp))
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(),
                label = ""
            )
            CustomLinearProgressBar(animatedProgress)
        }
    }
}

@Composable
private fun CustomLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(height)
            .border(
                width = 2.dp,
                color = CustomTheme.colors.d10,
                shape = CustomTheme.shapes.small
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(CustomTheme.shapes.small)
                .background(CustomTheme.colors.d10)
        )
    }
}

internal fun <T: Any> NavHostController.navigateSingleTop(route: T) = navigate(route) {
    launchSingleTop = true
}