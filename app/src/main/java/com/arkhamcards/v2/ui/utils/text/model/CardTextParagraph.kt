package com.arkhamcards.v2.ui.utils.text.model

import androidx.compose.ui.text.AnnotatedString

data class CardTextParagraph(
    val text: AnnotatedString = AnnotatedString(""),
    val alignment: ParagraphAlignment = ParagraphAlignment.Start,
    val blockQuote: Boolean = false,
    val horizontalRule: Boolean = false
)

enum class ParagraphAlignment {
    Start,
    Center,
    End
}