package com.arkhamcards.v2.ui.utils.text.model

data class TextSegment(
    val text: String,

    val style: StyleFlags,

    val annotation: String? = null,
)
