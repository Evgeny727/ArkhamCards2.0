package com.arkhamcards.v2.ui.utils.text.model

import com.arkhamcards.v2.ui.utils.text.model.StyleMask

internal class ParserState(
    flavorText: Boolean,
) {

    val stack = ArrayDeque<Tag>()

    val style = Int

    init {
        if (flavorText) {
            push(Tag.Italic)
        }
    }

    fun push(tag: Tag) {

        stack.addLast(tag)

        when (tag) {

            Tag.Bold -> style = StyleMask.BOLD

            Tag.Italic -> style.italic = true

            Tag.Underline -> style.underline = true

            Tag.Strike -> style.strike = true

            Tag.Trait -> {
                style.bold = true
                style.italic = true
                style.trait = true
            }

            Tag.Red ->
                style.red = true

            Tag.SmallCaps ->
                style.smallCaps = true

            Tag.MiniCaps ->
                style.miniCaps = true

            Tag.Fancy ->
                style.fancy = true

            Tag.FancyUnderline -> {
                style.fancyUnderline = true
                style.underline = true
            }

            Tag.Typewriter ->
                style.typewriter = true

            Tag.Cite ->
                style.cite = true

            Tag.Innsmouth ->
                style.innsmouth = true

            Tag.Game ->
                style.game = true

            Tag.Center,
            Tag.Right,
            Tag.BlockQuote,
            Tag.Link -> Unit
        }
    }

    fun pop(tag: Tag) {

        val index = stack.lastIndexOf(tag)

        if (index == -1)
            return

        stack.removeAt(index)

        rebuildFlags()
    }

    private fun rebuildFlags() {

        style.bold = false
        style.italic = false
        style.underline = false
        style.strike = false
        style.red = false
        style.trait = false
        style.smallCaps = false
        style.miniCaps = false
        style.fancy = false
        style.fancyUnderline = false
        style.typewriter = false
        style.cite = false
        style.innsmouth = false
        style.game = false

        stack.forEach(::applyTag)
    }

    private fun applyTag(tag: Tag) {

        when (tag) {

            Tag.Bold ->
                style.bold = true

            Tag.Italic ->
                style.italic = true

            Tag.Underline ->
                style.underline = true

            Tag.Strike ->
                style.strike = true

            Tag.Trait -> {
                style.bold = true
                style.italic = true
                style.trait = true
            }

            Tag.Red ->
                style.red = true

            Tag.SmallCaps ->
                style.smallCaps = true

            Tag.MiniCaps ->
                style.miniCaps = true

            Tag.Fancy ->
                style.fancy = true

            Tag.FancyUnderline -> {
                style.fancyUnderline = true
                style.underline = true
            }

            Tag.Typewriter ->
                style.typewriter = true

            Tag.Cite ->
                style.cite = true

            Tag.Innsmouth ->
                style.innsmouth = true

            Tag.Game ->
                style.game = true

            else -> {}
        }
    }
}