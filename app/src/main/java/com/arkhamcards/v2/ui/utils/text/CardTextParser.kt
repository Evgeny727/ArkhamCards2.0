package com.arkhamcards.v2.ui.utils.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.arkhamcards.v2.ui.utils.text.model.CardTextParagraph
import com.arkhamcards.v2.ui.utils.text.model.CardTextStyles
import com.arkhamcards.v2.ui.utils.text.model.IconRegistry
import com.arkhamcards.v2.ui.utils.text.model.ParagraphAlignment
import kotlinx.collections.immutable.ImmutableList

object CardTextParser {

    private data class ParseContext(
        val styles: CardTextStyles,
        val paragraphs: ParagraphBuilder
    )

    fun parse(
        text: String,
        processBullets: Boolean = false,
        styles: CardTextStyles
    ): ImmutableList<CardTextParagraph> {

        val paragraphs = ParagraphBuilder()

        paragraphs.currentBuilder.pushStyle(styles.regular)

        val processedText = text.preprocessCardText(processBullets)

        val context = ParseContext(
            styles = styles,
            paragraphs = paragraphs
        )

        parseSegment(
            text = processedText,
            index = 0,
            endTag = null,
            context = context
        )

        paragraphs.currentBuilder.pop()

        return paragraphs.build()
    }

    private fun parseSegment(
        text: String,
        index: Int,
        endTag: String?,
        context: ParseContext,
    ): Int {
        var current = index

        while (current < text.length) {

            if (endTag != null && text.startsWith(endTag, current)) {
                return current + endTag.length
            }

            when(text[current]) {

                '<' -> current = parseTag(
                    text = text,
                    index = current,
                    context = context,
                )

                '[' -> current = parseBracket(
                    text = text,
                    index = current,
                    context = context,
                )

                else -> current = parsePlainText(
                    text = text,
                    index = current,
                    builder = context.paragraphs.currentBuilder
                )
            }
        }

        return current
    }

    private fun parsePlainText(
        text: String,
        index: Int,
        builder: AnnotatedString.Builder,
    ): Int {

        var end = index

        while (
            end < text.length &&
            text[end] != '<' &&
            text[end] != '['
        ) {
            end++
        }

        builder.append(
            text,
            index,
            end
        )

        return end
    }

    private fun parseTag(
        text: String,
        index: Int,
        context: ParseContext,
    ): Int {
        val end = text.indexOf('>', index)

        if (end == -1) {
            context.paragraphs.currentBuilder.append('<')
            return index + 1
        }

        val tag = text.substring(index + 1, end)

        if (tag.startsWith('/')) {
            return end + 1
        }

        when(tag) {

            "p" -> {
                context.paragraphs.finishParagraph()

                return  end + 1
            }

            "br", "br/" -> {

                context.paragraphs.currentBuilder.append('\n')

                return end + 1
            }

            "hr", "hr/" -> {

                context.paragraphs.horizontalRule()

                return end + 1
            }

            "center" -> {
                context.paragraphs.finishParagraph()

                val old = context.paragraphs.alignment

                context.paragraphs.alignment = ParagraphAlignment.Center

                val next = parseSegment(
                    text,
                    end + 1,
                    "</center>",
                    context
                )

                context.paragraphs.finishParagraph()

                context.paragraphs.alignment = old

                return next
            }

            "right" -> {
                context.paragraphs.finishParagraph()

                val old = context.paragraphs.alignment

                context.paragraphs.alignment = ParagraphAlignment.End

                val next = parseSegment(
                    text,
                    end + 1,
                    "</right>",
                    context
                )

                context.paragraphs.finishParagraph()

                context.paragraphs.alignment = old

                return next
            }

            "blockquote" -> {
                context.paragraphs.finishParagraph()

                val previous = context.paragraphs.blockQuote

                context.paragraphs.blockQuote = true

                val next = parseSegment(
                    text,
                    end + 1,
                    "</blockquote>",
                    context
                )

                context.paragraphs.finishParagraph()

                context.paragraphs.blockQuote = previous

                return next
            }

            "b", "strong" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.bold,
                context
            )

            "i", "em" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.italic,
                context
            )

            "u" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.underline,
                context
            )

            "strike", "del" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.strike,
                context
            )

            "cite" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.cite,
                context
            )

            "trait" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.boldItalic,
                context
            )

            "red" -> return parseStyledTag(
                text,
                end + 1,
                "</$tag>",
                context.styles.red,
                context
            )

            else -> {
                context.paragraphs.currentBuilder.append(text, index, end + 1)

                return end + 1
            }
        }
    }

    private fun parseStyledTag(
        text: String,
        start: Int,
        closingTag: String,
        style: SpanStyle,
        context: ParseContext,
    ): Int {
        context.paragraphs.currentBuilder.pushStyle(style)

        val next = parseSegment(
            text,
            start,
            closingTag,
            context
        )

        context.paragraphs.currentBuilder.pop()

        return next
    }

    private fun parseBracket(
        text: String,
        index: Int,
        context: ParseContext,
    ): Int {
        val end = text.indexOf(']', index)

        if (end == -1) {
            context.paragraphs.currentBuilder.append('[')
            return index + 1
        }

        val key = text.substring(index + 1, end)

        val glyph = IconRegistry.glyph(key)

        if (glyph != null) {
            context.paragraphs.currentBuilder.withStyle(context.styles.icon) {
                context.paragraphs.currentBuilder.append(glyph)
            }
        } else {
            context.paragraphs.currentBuilder.append(
                text,
                index,
                end + 1
            )
        }

        return end + 1
    }
}