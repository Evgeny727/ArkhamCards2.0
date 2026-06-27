package com.arkhamcards.v2.data.objects

import java.text.Normalizer

private val SEARCH_REGEX = Regex("""["“”‹›«»〞〝〟„＂❝❞‘’❛❜‛",‚❮❯\(\)\-\.…¡!?¿]""")

fun String.normalizeForSearch(): String {
    if (isBlank()) return ""
    return Normalizer.normalize(this, Normalizer.Form.NFKD)
        .replace("\\p{Mn}+", "")
        .replace(SEARCH_REGEX, "")
        .lowercase()
}