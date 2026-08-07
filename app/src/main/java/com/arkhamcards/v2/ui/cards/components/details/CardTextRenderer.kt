package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.appSp
import com.arkhamcards.v2.ui.utils.text.CardTextParser
import com.arkhamcards.v2.ui.utils.text.model.CardTextStyles
import com.arkhamcards.v2.ui.utils.text.model.ParagraphAlignment

@Composable
fun ParsedCardText(
    text: String,
    modifier: Modifier = Modifier,
    isFlavor: Boolean = false,
) {
    val styles = rememberCardTextStyles(isFlavor)

    val paragraphs = remember(text, styles) {
        CardTextParser.parse(
            text,
            processBullets = !isFlavor,
            styles
        )
    }

    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
    ) {
        if (!isFlavor) {
            VerticalDivider(thickness = 2.dp, color = CustomTheme.colors.m)
        }

        Column(
            modifier = modifier.weight(1f).padding(
                start = if (isFlavor) 0.dp else 8.dp,
                end = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            paragraphs.forEach { paragraph ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (paragraph.blockQuote) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            VerticalDivider(thickness = 1.dp, color = CustomTheme.colors.m)
                            VerticalDivider(thickness = 1.dp, color = CustomTheme.colors.m)
                        }
                    }

                    Text(
                        text = paragraph.text,
                        fontSize = 16.appSp(CustomTheme.typography.scaleFactor),
                        lineHeight = 20.appSp(CustomTheme.typography.scaleFactor),
                        textAlign = when (paragraph.alignment) {
                            ParagraphAlignment.Start -> TextAlign.Start
                            ParagraphAlignment.Center -> TextAlign.Center
                            ParagraphAlignment.End -> TextAlign.End
                        },
                        color = CustomTheme.colors.darkText
                    )
                }

                if (paragraph.horizontalRule) {
                    HorizontalDivider(thickness = 1.dp, color = CustomTheme.colors.m)
                }
            }

        }
    }
}

@Composable
fun rememberCardTextStyles(
    flavorText: Boolean
): CardTextStyles {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    return remember(colors, typography, flavorText) {

        CardTextStyles(
            regular = (if (flavorText) typography.italic else typography.regular).toSpanStyle(),
            bold = typography.bold.toSpanStyle(),
            italic = (if (flavorText) typography.regular else typography.italic).toSpanStyle(),
            boldItalic = typography.boldItalic.toSpanStyle(),
            underline = typography.underline,
            strike = typography.strikethrough,
            red = typography.regular.toSpanStyle().copy(color = colors.campaign.text.resolution),
            game = SpanStyle(
                fontStyle = FontStyle.Normal,
                fontFamily = typography.gameFont.fontFamily,
                fontSize = 24.appSp(typography.scaleFactor)
            ),
            cite = typography.tiny.toSpanStyle(),
            icon = SpanStyle(fontFamily = AppIconsFont)
        )

    }
}