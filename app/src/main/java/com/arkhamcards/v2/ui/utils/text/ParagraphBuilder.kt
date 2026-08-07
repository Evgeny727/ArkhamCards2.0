package com.arkhamcards.v2.ui.utils.text

import androidx.compose.ui.text.AnnotatedString
import com.arkhamcards.v2.ui.utils.text.model.CardTextParagraph
import com.arkhamcards.v2.ui.utils.text.model.ParagraphAlignment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class ParagraphBuilder {

    private val paragraphs = mutableListOf<CardTextParagraph>()

    var alignment: ParagraphAlignment = ParagraphAlignment.Start

    var blockQuote: Boolean = false

    var horizontalRule: Boolean = false

    private var builder = AnnotatedString.Builder()

    /**
     * Builder used by parser.
     */
    val currentBuilder: AnnotatedString.Builder
        get() = builder

    /**
     * False if current paragraph already contains text.
     */
    val isEmpty: Boolean
        get() = builder.length == 0

    /**
     * Finishes current paragraph.
     *
     * Empty paragraphs are ignored.
     */
    fun finishParagraph() {

        if (isEmpty) return

        paragraphs += CardTextParagraph(
            text = builder.toAnnotatedString(),
            alignment = alignment,
            blockQuote = blockQuote,
            horizontalRule = horizontalRule
        )

        builder = AnnotatedString.Builder()
    }

    /**
     * Inserts horizontal divider.
     */
    fun horizontalRule() {

        horizontalRule = true

        finishParagraph()
    }

    /**
     * Returns final result.
     */
    fun build(): ImmutableList<CardTextParagraph> {

        finishParagraph()

        return paragraphs.toImmutableList()
    }
}