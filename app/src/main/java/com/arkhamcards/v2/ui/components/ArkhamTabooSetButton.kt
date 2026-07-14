package com.arkhamcards.v2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.R
import com.arkhamcards.v2.domain.model.meta.TabooSet
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.CustomTheme
import kotlinx.collections.immutable.ImmutableList
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ArkhamTabooSetButton(
    tabooSetId: Int,
    tabooSetsList: ImmutableList<TabooSet>,
    includeCurrent: Boolean = false,
    loading: Boolean = false,
    onTabooSetChange: (Int) -> Unit,
) {
    val none = stringResource(R.string.none)
    val current = stringResource(R.string.latest_taboo_set)
    val tabooSetName = when (tabooSetId) {
        100 -> {
            if (includeCurrent) current
            else tabooSetsList.firstOrNull()?.let {
                "${it.name} (${it.date.toLocalizedDate()})"
            } ?: stringResource(R.string.unknown)
        }
        0 -> none
        else -> tabooSetsList.find { it.id == tabooSetId }?.let {
            "${it.name} (${it.date.toLocalizedDate()})"
        } ?: stringResource(R.string.unknown)
    }
    var showTabooPicker by remember { mutableStateOf(false) }

    ArkhamSurfaceButton(
        title = stringResource(R.string.taboo_set),
        icon = AppIcon.Taboo,
        valueLabel = tabooSetName,
        loading = loading,
        editable = !loading,
        onClick = { showTabooPicker = true }
    )

    if (showTabooPicker) ArkhamDialog(
        title = stringResource(R.string.taboo_set),
        onDismiss = { showTabooPicker = false },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.6f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(0) {
                ArkhamCheckboxButton(
                    title = none,
                    isSelected = tabooSetId == 0,
                    enabled = tabooSetId != 0,
                    isRadio = true
                ) {
                    showTabooPicker = false
                    onTabooSetChange(0)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = CustomTheme.colors.divider)
            }
            if (includeCurrent) item(100) {
                ArkhamCheckboxButton(
                    title = current,
                    isSelected = tabooSetId == 100,
                    enabled = tabooSetId != 100,
                    isRadio = true
                ) {
                    showTabooPicker = false
                    onTabooSetChange(100)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = CustomTheme.colors.divider)
            }

            tabooSetsList.forEach { tabooSet ->
                item(tabooSet.id) {
                    ArkhamCheckboxButton(
                        title = "${tabooSet.name} (${tabooSet.date.toLocalizedDate()})",
                        isSelected = tabooSetId == tabooSet.id,
                        enabled = tabooSetId != tabooSet.id,
                        isRadio = true
                    ) {
                        showTabooPicker = false
                        onTabooSetChange(tabooSet.id)
                    }
                }
                item {
                    HorizontalDivider(color = CustomTheme.colors.divider)
                }
            }
        }
    }
}

@ReadOnlyComposable
@Composable
internal fun String.toLocalizedDate(): String {
    val locale = LocalLocale.current.platformLocale
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)

    return parser.parse(this)?.let(formatter::format).orEmpty()
}