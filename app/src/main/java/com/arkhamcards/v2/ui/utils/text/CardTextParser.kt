package com.arkhamcards.v2.ui.utils.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.withStyle
import com.arkhamcards.v2.ui.utils.text.model.IconRegistry
import com.arkhamcards.v2.ui.utils.text.model.Tag

internal class CardTextParser(
    private val builder: AnnotatedString.Builder,
    private val styles: StyleResolver,
) : TokenConsumer {

    private var styleMask = 0
    private val stack = ArrayDeque<Tag>()

    @ReadOnlyComposable
    @Composable
    override fun text(
        text: CharSequence,
        start: Int,
        end: Int,
    ) {

        builder.withStyle(
            styles.style(styleMask)
        ) {
            append(
                text,
                start,
                end
            )
        }
    }

    override fun icon(
        name: CharSequence
    ) {

        val glyph =
            IconRegistry.glyph(name)
                ?: return

        builder.withStyle(
            styles.iconStyle
        ) {

            append(glyph)

        }
    }

    override fun startTag(tag: Tag) {
        TODO("Not yet implemented")
    }

    override fun endTag(tag: Tag) {
        TODO("Not yet implemented")
    }

    override fun lineBreak() {
        TODO("Not yet implemented")
    }

    override fun horizontalRule() {
        TODO("Not yet implemented")
    }
}