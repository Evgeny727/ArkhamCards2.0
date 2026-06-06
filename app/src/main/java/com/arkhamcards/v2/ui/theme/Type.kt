package com.arkhamcards.v2.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhamcards.v2.R

@Immutable
data class CustomTypography(
    val cardHeaderHeight: Dp,
    val cursive: TextStyle,
    val searchLabel: TextStyle,
    val smallLabel: TextStyle,
    val smallButtonLabel: TextStyle,
    val small: TextStyle,
    val tiny: TextStyle,
    val menuText: TextStyle,
    val cardName: TextStyle,
    val cardTraits: TextStyle,
    val counter: TextStyle,
    val large: TextStyle,
    val header: TextStyle,
    val button: TextStyle,
    val subHeaderText: TextStyle,
    val text: TextStyle,
    val regular: TextStyle,
    val bold: TextStyle,
    val boldItalic: TextStyle,
    val italic: TextStyle,
    val simpleTitleFont: TextStyle,
    val gameFont: TextStyle,
    val mediumGameFont: TextStyle,
    val bigGameFont: TextStyle,
    val dialogLabel: TextStyle,
    val left: ParagraphStyle,
    val right: ParagraphStyle,
    val center: ParagraphStyle,
    val strikethrough: SpanStyle,
    val underline: SpanStyle,
)

val Caveat = FontFamily(
    Font(R.font.caveat, FontWeight.Normal),
)

val Alegreya = FontFamily(
    Font(R.font.alegreya_regular, FontWeight.Normal),
    Font(R.font.alegreya_medium, FontWeight.Medium),
    Font(R.font.alegreya_bold, FontWeight.Bold),
    Font(R.font.alegreya_extrabold, FontWeight.ExtraBold),
    Font(R.font.alegreya_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.alegreya_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.alegreya_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.alegreya_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
)

val PingFangTC = FontFamily(
    Font(R.font.pingfangtc_light, FontWeight.Normal),
    Font(R.font.pingfangtc_semibold, FontWeight.SemiBold),
)

val TeutonicRU = FontFamily(
    Font(R.font.teutonic_ru, FontWeight.Normal),
)

val Arkhamic = FontFamily(
    Font(R.font.arkhamic, FontWeight.Normal),
)

internal fun typography(
    colorScheme: CustomColors,
    lang: String,
    usePingFang: Boolean
): CustomTypography {
    val italicFont = if (usePingFang) PingFangTC else Alegreya
    val gameFont = if (lang == "ru") TeutonicRU else Arkhamic
    return typography.copy(
        cardHeaderHeight = 18.dp + if (lang == "zh" || lang == "zh-CN") 24.dp else 22.dp,
        cursive = TextStyle(
            fontFamily = Caveat,
            fontSize = 22.sp,
            lineHeight = 24.sp,
            color = colorScheme.d30
        ),
        searchLabel = TextStyle(
            fontFamily = Alegreya,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = colorScheme.l20
        ),
        smallLabel = TextStyle(
            fontFamily = Alegreya,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.3.sp,
            color = colorScheme.lightText
        ),
        smallButtonLabel = TextStyle(
            fontFamily = italicFont,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 17.sp else 16.sp,
            letterSpacing = 0.3.sp,
            color = colorScheme.lightText
        ),
        small = TextStyle(
            fontFamily = Alegreya,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colorScheme.darkText
        ),
        tiny = TextStyle(
            fontFamily = Alegreya,
            fontSize = 12.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 16.sp else 14.sp,
            color = colorScheme.darkText
        ),
        menuText = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            color = colorScheme.d30,
        ),
        cardName = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 22.sp else 20.sp,
            color = colorScheme.darkText,
        ),
        cardTraits = TextStyle(
            fontFamily = italicFont,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colorScheme.lightText
        ),
        counter = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 28.sp else 26.sp,
            color = colorScheme.d10,
        ),
        large = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 22.sp else 20.sp,
            color = colorScheme.darkText,
        ),
        header = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 26.sp else 24.sp,
            color = colorScheme.darkText,
        ),
        button = TextStyle(
            fontFamily = Alegreya,
            fontSize = 18.sp,
            lineHeight = if (lang == "zh" || lang == "zh-CN") 22.sp else 20.sp,
            color = colorScheme.l30,
        ),
        subHeaderText = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = colorScheme.d10,
        ),
        text = TextStyle(
            fontFamily = Alegreya,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = colorScheme.darkText,
        ),
        regular = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Normal,
        ),
        bold = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Bold,
            color = colorScheme.darkText
        ),
        boldItalic = TextStyle(
            fontFamily = italicFont,
            fontWeight = if (usePingFang) FontWeight.SemiBold else FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            color = colorScheme.darkText
        ),
        italic = TextStyle(
            fontFamily = italicFont,
            fontStyle = FontStyle.Italic,
        ),
        simpleTitleFont = TextStyle(
            fontFamily = Alegreya,
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            color = colorScheme.darkText,
        ),
        gameFont = TextStyle(
            fontFamily = gameFont,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            color = colorScheme.darkText,
        ),
        mediumGameFont = TextStyle(
            fontFamily = gameFont,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            color = colorScheme.darkText,
        ),
        bigGameFont = TextStyle(
            fontFamily = gameFont,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            color = colorScheme.darkText,
        ),
        dialogLabel = TextStyle(
            fontSize = 16.sp, //13 for iOS
            color = colorScheme.darkText,
        ),
    )
}

val typography = CustomTypography(
    cardHeaderHeight = 40.dp,
    cursive = TextStyle(),
    searchLabel = TextStyle(),
    smallLabel = TextStyle(),
    smallButtonLabel = TextStyle(),
    small = TextStyle(),
    tiny = TextStyle(),
    menuText = TextStyle(),
    cardName = TextStyle(),
    cardTraits = TextStyle(),
    counter = TextStyle(),
    large = TextStyle(),
    header = TextStyle(),
    button = TextStyle(),
    subHeaderText = TextStyle(),
    text = TextStyle(),
    regular = TextStyle(),
    bold = TextStyle(),
    boldItalic = TextStyle(),
    italic = TextStyle(),
    simpleTitleFont = TextStyle(),
    gameFont = TextStyle(),
    mediumGameFont = TextStyle(),
    bigGameFont = TextStyle(),
    dialogLabel = TextStyle(),
    left = ParagraphStyle(textAlign = TextAlign.Start),
    right = ParagraphStyle(textAlign = TextAlign.End),
    center = ParagraphStyle(textAlign = TextAlign.Center),
    strikethrough = SpanStyle(textDecoration = TextDecoration.LineThrough),
    underline = SpanStyle(textDecoration = TextDecoration.Underline),
)

val LocalCustomTypography = staticCompositionLocalOf { typography }