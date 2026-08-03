package com.arkhamcards.v2.ui.cards.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.arkhamcards.v2.ui.theme.CustomTheme

@Composable
fun CardDetailsSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    normalCase: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "— $title —".let { if (normalCase) it else it.uppercase() },
            style = CustomTheme.typography.large,
            color = CustomTheme.colors.d10,
            textAlign = TextAlign.Center,
        )
    }
}