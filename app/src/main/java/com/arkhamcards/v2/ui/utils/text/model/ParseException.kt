package com.arkhamcards.v2.ui.utils.text.model

data class ParseException(
    override val message: String,
    val position: Int
) : RuntimeException(message)
