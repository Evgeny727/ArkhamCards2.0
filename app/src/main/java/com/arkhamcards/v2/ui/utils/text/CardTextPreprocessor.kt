package com.arkhamcards.v2.ui.utils.text

val WEIRD_BULLET_REGEX = Regex("""\\u2022""")
val BAD_LINEBREAK_REGEX = Regex("""\/n""")
val INDENTED_BULLET_REGEX = Regex("""(^\s?--|^-—\s+)([^0-9].+)$""")
val BULLET_REGEX = Regex("""(^\s?-|^—\s+)([^0-9].+)$""")
val GUIDE_BULLET_REGEX = Regex("""(^\s?=|^=\s+)([^0-9].+)$""")
val PARAGRAPH_BULLET_REGEX = Regex("""(<p>- )|(<p>–)""")
val DOUBLE_BRACKET_REGEX = Regex("""\[\[([^\]]+)\]\]""")

internal fun String.preprocessCardText(processBullets: Boolean): String {
    val result = this
        .replace(WEIRD_BULLET_REGEX, "•")
        .replace(BAD_LINEBREAK_REGEX, "\n")
        .replace(DOUBLE_BRACKET_REGEX) { match ->
            "<trait>${match.groupValues[1]}</trait>"
        }

    if (processBullets) {
        return result
            .replace(INDENTED_BULLET_REGEX) { match ->
                "\t[bullet] ${match.groupValues[2]}"
            }
            .replace(BULLET_REGEX) { match ->
                "[bullet] ${match.groupValues[2]}"
            }
            .replace(GUIDE_BULLET_REGEX) { match ->
                "[guide_bullet] ${match.groupValues[2]}"
            }
            .replace(PARAGRAPH_BULLET_REGEX, "<p>[bullet]")
    }

    return result
}