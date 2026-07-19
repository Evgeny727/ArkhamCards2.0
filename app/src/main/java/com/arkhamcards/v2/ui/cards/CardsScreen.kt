package com.arkhamcards.v2.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.arkhamcards.v2.ui.cards.components.CardListItem
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.appSp
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardsScreen(
    viewModel: CardsViewModel,
    emitError: (Throwable) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    val density = LocalDensity.current
    val rowHeight = with(density) {
        (26 + 21).appSp(CustomTheme.typography.scaleFactor).toDp()
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

    LazyColumn(
        modifier = modifier.applyScaffoldPaddings(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 8.dp),
        state = listState
    ) {
        items(
            count = searchResults.itemCount,
            key = searchResults.itemKey { it.id },
            contentType = searchResults.itemContentType { it::class }
        ) { index ->
            val item = searchResults[index] ?: return@items
            CardListItem(
                cardListItem = item,
                rowHeight = rowHeight,
                onClick = { /*TODO*/ }
            )
        }
        // Handle load states: initial load and pagination load errors/loading.
        searchResults.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = CustomTheme.colors.m
                            )
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
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
    }
}