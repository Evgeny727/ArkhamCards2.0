package com.arkhamcards.v2.ui.utils.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.arkhamcards.v2.ui.utils.text.model.Tag

internal class CardTextTokenizer(
    private val consumer: TokenConsumer,
) {

    @ReadOnlyComposable
    @Composable
    fun Tokenize(text: String) {

        var index = 0
        var textStart = 0

        while (index < text.length) {

            when (text[index]) {

                '[' -> {

                    flushText(
                        text,
                        textStart,
                        index
                    )

                    index = parseIcon(text, index)

                    textStart = index
                }

                '<' -> {

                    flushText(
                        text,
                        textStart,
                        index
                    )

                    index = parseTag(text, index)

                    textStart = index
                }

                '\n' -> {

                    flushText(
                        text,
                        textStart,
                        index
                    )

                    consumer.lineBreak()

                    index++

                    textStart = index
                }

                else -> index++
            }
        }

        flushText(
            text,
            textStart,
            text.length
        )
    }

    @ReadOnlyComposable
    @Composable
    private fun flushText(
        source: String,
        start: Int,
        end: Int,
    ) {

        if (start == end)
            return

        consumer.text(
            source,
            start,
            end
        )
    }

    @ReadOnlyComposable
    @Composable
    private fun parseIcon(
        text: String,
        start: Int,
    ): Int {

        val end = text.indexOf(']', start)

        if (end == -1) {
            consumer.text(
                text,
                start,
                text.length
            )
            return text.length
        }

        val name = text.substring(
            start + 1,
            end
        )

        consumer.icon(name)

        return end + 1
    }

    @ReadOnlyComposable
    @Composable
    private fun parseTag(
        text: String,
        start: Int,
    ): Int {

        val end = text.indexOf('>', start)

        if (end == -1) {

            consumer.text(
                text,
                start,
                text.length
            )

            return text.length
        }

        val raw = text.substring(
            start + 1,
            end
        )

        when {

            raw == "br/" -> consumer.lineBreak()

            raw == "hr/" -> consumer.horizontalRule()

            raw.startsWith('/') -> {
                TagLookup.find(raw.substring(1))?.let { consumer.endTag(it) }
            }

            else -> {
                TagLookup.find(raw)?.let { consumer.startTag(it) }
            }
        }

        return end + 1
    }
}

internal interface TokenConsumer {

    @ReadOnlyComposable
    @Composable
    fun text(
        text: CharSequence,
        start: Int,
        end: Int,
    )


    fun icon(name: CharSequence)

    fun startTag(tag: Tag)

    fun endTag(tag: Tag)

    fun lineBreak()

    fun horizontalRule()
}

internal object TagLookup {

    private val tags = hashMapOf(

        "b" to Tag.Bold,
        "strong" to Tag.Bold,

        "i" to Tag.Italic,
        "em" to Tag.Italic,

        "u" to Tag.Underline,

        "strike" to Tag.Strike,
        "del" to Tag.Strike,

        "trait" to Tag.Trait,

        "red" to Tag.Red,

        "smallcaps" to Tag.SmallCaps,

        "minicaps" to Tag.MiniCaps,

        "center" to Tag.Center,

        "right" to Tag.Right,

        "blockquote" to Tag.BlockQuote,

        "cite" to Tag.Cite,

        "typewriter" to Tag.Typewriter,

        "game" to Tag.Game,

        "fancy" to Tag.Fancy,

        "fancy_u" to Tag.FancyUnderline,

        "innsmouth" to Tag.Innsmouth,
    )

    fun find(name: String): Tag? =
        tags[name]
}