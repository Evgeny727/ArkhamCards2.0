package com.arkhamcompanion.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.arkhamcompanion.R
import com.arkhamcompanion.domain.model.cards.CardListItemUiModel
import com.arkhamcompanion.ui.cards.components.CardListItem
import com.arkhamcompanion.ui.cards.components.CardSectionHeader
import com.arkhamcompanion.ui.cards.components.PlaceholderCardListItem
import com.arkhamcompanion.ui.cards.components.buildHeaderTitle
import com.arkhamcompanion.ui.components.ArkhamButton
import com.arkhamcompanion.ui.components.ArkhamButtonSearchIcon
import com.arkhamcompanion.ui.components.ArkhamSearchBox
import com.arkhamcompanion.ui.components.CardsSearchOptions
import com.arkhamcompanion.ui.theme.CustomTheme
import com.arkhamcompanion.ui.utils.applyScaffoldPaddings
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardsScreen(
    viewModel: CardsViewModel,
    emitError: (Throwable) -> Unit,
    onCardClick: (String) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val spoilerState by viewModel.spoilerState.collectAsState()
    val searchOptions by viewModel.searchOptions.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val density = LocalDensity.current
    val rowHeight = with(density) {
        maxOf(
            a = 36.dp,
            b = (47 * CustomTheme.typography.scaleFactor * fontScale).dp
        )
    }

    LaunchedEffect(Unit) {
        viewModel.errors.collect {
            emitError(it.exception)
        }
    }
    // Whenever the search query changes, scroll the list back to the top.
    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            listState.scrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .applyScaffoldPaddings(innerPadding)
            .fillMaxSize(),
    ) {
        ArkhamSearchBox(
            searchQuery = searchOptions.searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            onClearQuery = viewModel::clearSearchQuery,
            searchPlaceholder = stringResource(R.string.search_for_a_card)
        ) {
            CardsSearchOptions(
                searchGame = searchOptions.searchGame,
                onSearchGameChange = viewModel::onSearchGameTextChange,
                searchFlavor = searchOptions.searchFlavor,
                onSearchFlavorChange = viewModel::onSearchFlavorTextChange,
                searchBack = searchOptions.searchBack,
                onSearchBackChange = viewModel::onSearchBackTextChange
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp),
            state = listState
        ) {
            if (searchResults.itemCount == 0 && searchResults.loadState.isIdle) {
                item("no_results", contentType = "text") {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItem()
                    ) {
                        Text(
                            text = if (searchOptions.searchQuery.isBlank()) {
                                stringResource(R.string.no_matching_cards)
                            }
                            else {
                                stringResource(
                                    id = R.string.no_matching_cards_for_query,
                                    searchOptions.searchQuery
                                )
                            },
                            style = CustomTheme.typography.text,
                        )
                        if (searchOptions.searchQuery.isBlank()) {
                            Text(
                                text = stringResource(R.string.edit_collection_in_settings),
                                style = CustomTheme.typography.text,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Handle load states: initial load and pagination load errors/loading.
            searchResults.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item("loading", contentType = "text") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = CustomTheme.colors.m
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.searching_cards),
                                    style = CustomTheme.typography.text,
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item("appending", contentType = "text") {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = CustomTheme.colors.m
                                )
                            }
                        }
                    }
                }
            }

            items(
                count = searchResults.itemCount,
                key = searchResults.itemKey { when (it) {
                    is CardListItemUiModel.CategoryHeader -> it.key
                    is CardListItemUiModel.CardItem -> it.card.id
                } },
                contentType = searchResults.itemContentType { when (it) {
                    is CardListItemUiModel.CategoryHeader -> "header"
                    is CardListItemUiModel.CardItem -> "card"
                } }
            ) { index ->
                when (val item = searchResults[index]) {
                    null -> {
                        PlaceholderCardListItem(rowHeight = rowHeight)
                    }

                    is CardListItemUiModel.CategoryHeader -> {
                        val title = buildHeaderTitle(item.category, item.value)
                        CardSectionHeader(title)
                    }

                    is CardListItemUiModel.CardItem -> {
                        CardListItem(
                            cardListItem = item.card,
                            rowHeight = rowHeight,
                            onClick = {
                                onCardClick(item.card.code)
                            }
                        )
                    }
                }
            }

            if (searchOptions.searchQuery.isNotBlank()) {
                item("clear_search_button", contentType = "button") {
                    ArkhamButton(
                        title = stringResource(R.string.clear_query_search, searchOptions.searchQuery),
                        onClick = viewModel::clearSearchQuery,
                        modifier = Modifier
                            .padding(8.dp)
                            .animateItem(),
                    ) { color ->
                        ArkhamButtonSearchIcon(color)
                    }
                }

                if (!searchOptions.searchGame) {
                    item("search_game_button", contentType = "button") {
                        ArkhamButton(
                            title = stringResource(R.string.search_game_text),
                            onClick = { viewModel.onSearchGameTextChange(true) },
                            modifier = Modifier
                                .padding(8.dp)
                                .animateItem(),
                        ) { color ->
                            ArkhamButtonSearchIcon(color)
                        }
                    }
                }

                if (!searchOptions.searchFlavor) {
                    item("search_flavor_button", contentType = "button") {
                        ArkhamButton(
                            title = stringResource(R.string.search_flavor_text),
                            onClick = { viewModel.onSearchFlavorTextChange(true) },
                            modifier = Modifier
                                .padding(8.dp)
                                .animateItem(),
                        ) { color ->
                            ArkhamButtonSearchIcon(color)
                        }
                    }
                }

                if (!searchOptions.searchBack) {
                    item("search_back_button", contentType = "button") {
                        ArkhamButton(
                            title = stringResource(R.string.search_card_backs),
                            onClick = { viewModel.onSearchBackTextChange(true) },
                            modifier = Modifier
                                .padding(8.dp)
                                .animateItem(),
                        ) { color ->
                            ArkhamButtonSearchIcon(color)
                        }
                    }
                }

                item("search_player_encounter_button", contentType = "button") {
                    ArkhamButton(
                        title = stringResource(if (spoilerState) R.string.search_player_cards
                            else R.string.search_encounter_cards),
                        onClick = { viewModel.toggleSpoiler(!spoilerState) },
                        modifier = Modifier
                            .padding(8.dp)
                            .animateItem(),
                    ) { color ->
                        ArkhamButtonSearchIcon(color)
                    }
                }
            }
        }
    }
}