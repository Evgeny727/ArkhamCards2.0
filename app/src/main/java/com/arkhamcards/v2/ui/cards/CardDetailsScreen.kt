package com.arkhamcards.v2.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.applyScaffoldPaddings

@Composable
fun CardDetailsScreen(
    cardCode: String,
    cardsViewModel: CardsViewModel,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    val cardsLazyCodes by cardsViewModel.searchResultCodes.collectAsState()
    val index = remember(cardsLazyCodes) {
        cardsLazyCodes.indexOfFirst { it.code == cardCode }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(initialPage = index) { cardsLazyCodes.size }

    LaunchedEffect(cardsLazyCodes) {
        if (cardsLazyCodes.isNotEmpty()) {
            pagerState.scrollToPage(index)
        }
    }

    HorizontalPager(
        state = pagerState,
        key = { page -> cardsLazyCodes[page].code },
        modifier = modifier.fillMaxSize().applyScaffoldPaddings(innerPadding),
    ) { page ->
        val item = cardsLazyCodes[page]
        val cardDetailsWithRelations by cardsViewModel.getCardDetailsWithRelations(
            item.code,
            item.tabooSetId
        ).collectAsState(null)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            if (cardDetailsWithRelations == null) item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = CustomTheme.colors.m)
                }
            } else item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = cardDetailsWithRelations.toString(),
                        style = CustomTheme.typography.large
                    )
                }
            }
        }
    }
}