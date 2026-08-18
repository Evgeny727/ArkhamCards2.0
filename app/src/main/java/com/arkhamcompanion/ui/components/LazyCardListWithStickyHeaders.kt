package com.arkhamcompanion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.arkhamcompanion.R
import com.arkhamcompanion.domain.model.cards.CardListItemUiModel
import com.arkhamcompanion.domain.model.cards.CodeWithTaboo
import com.arkhamcompanion.ui.cards.components.CardListItem
import com.arkhamcompanion.ui.cards.components.CardSectionHeader
import com.arkhamcompanion.ui.cards.components.PlaceholderCardListItem
import com.arkhamcompanion.ui.cards.components.buildHeaderTitle
import com.arkhamcompanion.ui.theme.CustomTheme
import kotlinx.collections.immutable.ImmutableList
import kotlin.collections.set

@Composable
fun LazyCardListWithStickHeaders(
    searchQuery: String,
    searchResults: LazyPagingItems<CardListItemUiModel>,
    searchResultCodes: ImmutableList<CodeWithTaboo>,
    listState: LazyListState,
    rowHeight: Dp,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    bottomButtons: LazyListScope.() -> Unit,
) {
    val sectionBoundariesByCode by rememberSectionBoundaries(searchResults)
    val codeToIndex by remember(searchResultCodes) {
        derivedStateOf {
            searchResultCodes
                .mapIndexed { index, code ->
                    code.code to index
                }
                .toMap()
        }
    }
    val sectionBoundaries by remember(sectionBoundariesByCode, codeToIndex) {
        derivedStateOf {
            sectionBoundariesByCode
                .mapNotNull { (code, header) ->
                    codeToIndex[code]?.let { index ->
                        SectionBoundary(
                            firstCardIndex = index,
                            header = header
                        )
                    }
                }
                .sortedBy { it.firstCardIndex }
        }
    }
    val currentCardCode by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.firstNotNullOfOrNull { itemInfo ->
                searchResults.peekOrNull(itemInfo.index)?.let { item ->
                    item as? CardListItemUiModel.CardItem
                }?.card?.code
            }
        }
    }
    val currentCardIndex = currentCardCode?.let(codeToIndex::get)
    val currentHeader by remember(currentCardIndex, sectionBoundaries) {
        derivedStateOf {
            currentCardIndex?.let {
                findSection(
                    cardIndex = it,
                    boundaries = sectionBoundaries,
                )
            }
        }
    }
    val visibleHeader by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.firstNotNullOfOrNull { itemInfo ->
                searchResults.peekOrNull(itemInfo.index) as? CardListItemUiModel.CategoryHeader
            }
        }
    }
    val stickyHeader = currentHeader ?: visibleHeader
    val nextHeaderInfo by remember(stickyHeader) {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.firstNotNullOfOrNull { itemInfo ->
                searchResults.peekOrNull(itemInfo.index)?.let { item ->
                    if (
                        item is CardListItemUiModel.CategoryHeader &&
                        item.key != stickyHeader?.key
                    ) {
                        itemInfo
                    } else {
                        null
                    }
                }
            }
        }
    }
    var stickyHeaderHeightPx by remember { mutableIntStateOf(0) }
    val stickyOffsetPx by remember(nextHeaderInfo, stickyHeaderHeightPx) {
        derivedStateOf {
            if (nextHeaderInfo == null || stickyHeaderHeightPx == 0) {
                0
            } else {
                minOf(0, nextHeaderInfo!!.offset - stickyHeaderHeightPx)
            }
        }
    }

    Box(
        modifier = Modifier.clipToBounds()
    ) {
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
                            text = if (searchQuery.isBlank()) {
                                stringResource(R.string.no_matching_cards)
                            } else {
                                stringResource(
                                    id = R.string.no_matching_cards_for_query,
                                    searchQuery
                                )
                            },
                            style = CustomTheme.typography.text,
                        )
                        if (searchQuery.isBlank()) {
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

            if (searchQuery.isNotBlank()) bottomButtons()
        }

        stickyHeader?.let { header ->
            CardSectionHeader(
                title = buildHeaderTitle(header.category, header.value),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(
                            x = 0,
                            y = stickyOffsetPx
                        )
                    }
                    .onSizeChanged {
                        stickyHeaderHeightPx = it.height
                    }
            )
        }
    }
}

@Immutable
private data class SectionBoundary(
    val firstCardIndex: Int,
    val header: CardListItemUiModel.CategoryHeader,
)

@Composable
private fun rememberSectionBoundaries(
    searchResults: LazyPagingItems<CardListItemUiModel>,
): State<Map<String, CardListItemUiModel.CategoryHeader>> {
    val boundaries = remember {
        mutableStateMapOf<String, CardListItemUiModel.CategoryHeader>()
    }

    LaunchedEffect(searchResults) {
        snapshotFlow {
            searchResults.itemSnapshotList.items
        }.collect { items ->
            items
                .filterIsInstance<CardListItemUiModel.CategoryHeader>()
                .forEach { header ->
                    boundaries[header.firstCardCode] = header
                }
        }
    }

    return remember {
        derivedStateOf {
            boundaries.toMap()
        }
    }
}

private fun findSection(
    cardIndex: Int,
    boundaries: List<SectionBoundary>,
): CardListItemUiModel.CategoryHeader? {
    if (cardIndex < 0 || boundaries.isEmpty()) return null

    var result: CardListItemUiModel.CategoryHeader? = null

    for (boundary in boundaries) {
        if (boundary.firstCardIndex > cardIndex) {
            break
        }

        result = boundary.header
    }

    return result
}

private fun <T : Any> LazyPagingItems<T>.peekOrNull(index: Int): T? {
    if (index !in 0 until itemCount) {
        return null
    }

    return peek(index)
}