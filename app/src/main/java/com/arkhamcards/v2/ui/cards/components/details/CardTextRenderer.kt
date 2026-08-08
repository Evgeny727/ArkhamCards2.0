package com.arkhamcards.v2.ui.cards.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arkhamcards.v2.domain.model.cards.CardText
import com.arkhamcards.v2.domain.model.cards.CardTextParagraph
import com.arkhamcards.v2.domain.model.cards.CardTextSegment
import com.arkhamcards.v2.domain.model.cards.ParagraphAlignment
import com.arkhamcards.v2.ui.icons.AppIcon
import com.arkhamcards.v2.ui.theme.AppIconsFont
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.arkhamcards.v2.ui.utils.CardTextStyleResolver
import com.arkhamcards.v2.ui.utils.appSp

@Composable
fun ParsedCardText(
    text: CardText,
    styleResolver: CardTextStyleResolver,
    modifier: Modifier = Modifier,
    isFlavor: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
    ) {
        if (!isFlavor) {
            VerticalDivider(thickness = 2.dp, color = CustomTheme.colors.m)
        }

        Column(
            modifier = Modifier.weight(1f).padding(
                start = if (isFlavor) 0.dp else 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        ) {
            text.paragraphs.forEach { paragraph ->
                val paragraphText = remember(text.text, paragraph, styleResolver) {
                    paragraph.toAnnotatedString(text.text, styleResolver)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
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
                        text = paragraphText,
                        fontSize = 16.appSp(CustomTheme.typography.scaleFactor),
                        lineHeight = 20.appSp(CustomTheme.typography.scaleFactor),
                        textAlign = when (paragraph.alignment) {
                            ParagraphAlignment.Start -> TextAlign.Start
                            ParagraphAlignment.Center -> TextAlign.Center
                            ParagraphAlignment.End -> TextAlign.End
                        },
                        color = CustomTheme.colors.darkText,
                        modifier = Modifier.padding(vertical = if (paragraph.blockQuote) 4.dp else 0.dp)
                    )
                }

                if (paragraph.horizontalRule) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(thickness = 1.dp, color = CustomTheme.colors.m)
                    Spacer(modifier = Modifier.height(8.dp))
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

    return remember(colors, typography) {

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

private fun CardTextParagraph.toAnnotatedString(
    text: String,
    styleResolver: CardTextStyleResolver
): AnnotatedString {
    return buildAnnotatedString {
        segments.forEach { segment ->
            when (segment) {
                is CardTextSegment.Text -> {
                    withStyle(styleResolver.resolve(segment.styleFlags)) {
                        append(
                            text,
                            segment.start,
                            segment.end,
                        )
                    }
                }

                is CardTextSegment.Icon -> {
                    withStyle(styleResolver.getIconStyle()) {
                        val icon = AppIcon.fromNameCode(
                            when (segment.glyph) {
                                "fast" -> "free"
                                else -> segment.glyph
                            }
                        )
                        append(icon.glyph)
                    }
                }
            }
        }
    }
}