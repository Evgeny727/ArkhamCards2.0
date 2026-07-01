package com.arkhamcards.v2.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Grayscale
val Light30 = Color(0xFFFFFBF2)
val Light20 = Color(0xFFF5F0E1)
val Light15 = Color(0xFFE6E1D3)
val Light10 = Color(0xFFD7D3C6)
val Medium = Color(0xFF9B9B9B)
val Dark10 = Color(0xFF656C6F)
val Dark15 = Color(0xFF4F5A60)
val Dark20 = Color(0xFF475259)
val Dark30 = Color(0xFF24303C)

val NeutralBorder = Medium

// Light colors
val GuardianLightText = Color(0xFF1072C2)
val SeekerLightText = Color(0xFFDB7C07)
val RogueLightText = Color(0xFF219428)
val MysticLightText = Color(0xFF7554AB)
val SurvivorLightText = Color(0xFFCC3038)
val NeutralLightText = Dark20
val DualLightText = Color(0xFF868600)
val DeadLightText = Color(0xFF704214)
val MythosLightText = Dark30

// Dark colors
val GuardianDarkText = Color(0xFF5CB4FD)
val SeekerDarkText = Color(0xFFEFA345)
val RogueDarkText = Color(0xFF48B14F)
val MysticDarkText = Color(0xFFBA81F2)
val SurvivorDarkText = Color(0xFFEE4A53)
val NeutralDarkText = Light20
val DualDarkText = Color(0xFFE9D06C)
val DeadDarkText = Color(0xFF704214)
val MythosDarkText = Light30

// Permanent colors
val Blue = Color(0xFF1072C2)
val Orange = Color(0xFFDB7C07)
val Green = Color(0xFF219428)
val Red = Color(0xFFCC3038)
val Purple = Color(0xFF593B5D)
val Gold = Color(0xFFBFA640)

@Immutable
data class FactionColors(
    val invertedText: Color,
    val text: Color,
    val border: Color,
    val background: Color,
    val darkBackground: Color,
    val lightBackground: Color,
)

data class CampaignColors(
    val text: CampaignTextColors,
    val background: CampaignBackgroundColors,
    val core: Color,
    val dwl: Color,
    val ptc: Color,
    val tfa: Color,
    val tcu: Color,
    val tde: Color,
    val tic: Color,
    val eoe: Color,
    val tsk: Color,
    val standalone: Color
)

data class CampaignTextColors(
    val setup: Color,
    val interlude: Color,
    val resolution: Color
)

data class CampaignBackgroundColors(
    val setup: Color,
    val interlude: Color,
    val resolution: Color
)

data class TableColors(
    val header: Color,
    val light: Color,
    val dark: Color
)

data class TokenFillColors(
    val bless: Color,
    val curse: Color
)

data class FactionThemeColors(
    val guardian: FactionColors,
    val seeker: FactionColors,
    val rogue: FactionColors,
    val mystic: FactionColors,
    val survivor: FactionColors,
    val neutral: FactionColors,
    val mythos: FactionColors,
    val dual: FactionColors,
    val dead: FactionColors
)

data class SkillThemeColors(
    val willpower: Color,
    val intellect: Color,
    val combat: Color,
    val agility: Color,
    val wild: Color
)

data class TokenColors(
    val skull: Color,
    val cultist: Color,
    val tablet: Color,
    val elderThing: Color,
    val elderSign: Color,
    val autoFail: Color,
    val bless: Color,
    val curse: Color,
    val frost: Color
)


@Immutable
data class CustomColors(
    val fight: Color,
    val evade: Color,
    val l30: Color,
    val l20: Color,
    val l15: Color,
    val l10: Color,
    val m: Color,
    val d10: Color,
    val d15: Color,
    val d20: Color,
    val d30: Color,
    val background: Color,
    val darkText: Color,
    val lightText: Color,
    val taboo: Color,
    val divider: Color,
    val tarotText: Color,
    val tarotInvertedText: Color,
    val faction: FactionThemeColors,
    val skill: SkillThemeColors,
    val token: TokenColors,
    val tokenFill: TokenFillColors,
    val health: Color,
    val sanity: Color,
    val disableOverlay: Color,
    val campaign: CampaignColors,
    val warn: Color,
    val green: Color,
    val red: Color,
    val upgrade: Color,
    val warnText: Color,
    val table: TableColors
)

val DarkColorScheme = CustomColors(
    l30 = Dark30,
    l20 = Dark20,
    l15 = Dark15,
    l10 = Dark10,
    m = Medium,
    d10 = Light10,
    d15 = Light15,
    d20 = Light20,
    d30 = Light30,
    fight = Color(0xFFEE4A53),
    evade = Color(0xFF48B14F),
    background = Dark30,
    darkText = Light30,
    lightText = Light10,
    taboo = Color(0xFF9869f5),
    divider = Dark10,
    tarotText = DualDarkText,
    tarotInvertedText = DualLightText,
    faction = FactionThemeColors(
        guardian = FactionColors(
            invertedText = GuardianLightText,
            text = GuardianDarkText,
            border = GuardianDarkText,
            background = GuardianLightText,
            darkBackground = Color(0xFF2b80c5),
            lightBackground = Color(0xFF004880)
        ),
        seeker = FactionColors(
            invertedText = SeekerLightText,
            text = SeekerDarkText,
            border = SeekerDarkText,
            background = Color(0xFFDB7C07),
            darkBackground = Color(0xFFdb7c07),
            lightBackground = Color(0xFFbf5c00)
        ),
        rogue = FactionColors(
            invertedText = RogueLightText,
            text = RogueDarkText,
            border = RogueDarkText,
            background = Color(0xFF219428),
            darkBackground = Color(0xFF107116),
            lightBackground = Color(0xFF015906)
        ),
        mystic = FactionColors(
            invertedText = MysticLightText,
            text = MysticDarkText,
            border = MysticDarkText,
            background = Color(0xFF7554AB),
            darkBackground = Color(0xFF7554AB),
            lightBackground = Color(0xFF46018f)
        ),
        survivor = FactionColors(
            invertedText = SurvivorLightText,
            text = SurvivorDarkText,
            border = SurvivorDarkText,
            background = Color(0xFFCC3038),
            darkBackground = Color(0xFFCC3038),
            lightBackground = Color(0xFF7a0105)
        ),
        neutral = FactionColors(
            invertedText = NeutralLightText,
            text = NeutralDarkText,
            border = NeutralBorder,
            background = Dark20,
            darkBackground = Color(0xFF444444),
            lightBackground = Color(0xFF292929)
        ),
        dual = FactionColors(
            invertedText = DualLightText,
            text = DualDarkText,
            border = DualDarkText,
            background = Color(0xFFcfb13a),
            darkBackground = Color(0xFFc0c000),
            lightBackground = Color(0xFFf2f2cc)
        ),
        dead = FactionColors(
            invertedText = DeadLightText,
            text = DeadDarkText,
            border = DeadDarkText,
            background = Color(0xFF704214),
            darkBackground = Color(0xFF5a3510),
            lightBackground = Color(0xFFd4c6b9)
        ),
        mythos = FactionColors(
            invertedText = MythosLightText,
            text = MythosDarkText,
            border = MythosDarkText,
            background = Light30,
            darkBackground = Color(0xFF000000),
            lightBackground = Color(0xFF000000)
        )
    ),
    skill = SkillThemeColors(
        willpower = Color(0xFF2C7FC0),
        intellect = Color(0xFF7C3C85),
        combat = Color(0xFFAE4236),
        agility = Color(0xFF14854D),
        wild = Color(0xFF8A7D5A)
    ),
    token = TokenColors(
        skull = Color(0xFF915c5c),
        cultist = Color(0xFF669154),
        tablet = Color(0xFF548994),
        elderThing = Color(0xFFa661ab),
        elderSign = Color(0xFF5496cc),
        autoFail = Color(0xFFbf2128),
        bless = Color(0xFFebaa42),
        curse = Color(0xFFb069c9),
        frost = Color(0xFF6559f7)
    ),
    tokenFill = TokenFillColors(
        bless = Color(0xFFebaa42),
        curse = Color(0xFFb069c9)
    ),
    health = Color(0xFFAE4236),
    sanity = Color(0xFF2C7FC0),
    disableOverlay = Color(0x24303C99),
    campaign = CampaignColors(
        text = CampaignTextColors(
            setup = Color(0xFF07AF73),
            interlude = Color(0xFF4C97EF),
            resolution = Color(0xFFF04932)
        ),
        background = CampaignBackgroundColors(
            setup = Color(0x07AF7333),
            interlude = Color(0x4C97EF33),
            resolution = Color(0xF0493233)
        ),
        core = Color(0xFF006385),
        dwl = Color(0xFF57783A),
        ptc = Color(0xFF524F8D),
        tfa = Color(0xFF96558E),
        tcu = Color(0xFF4B314E),
        tde = Color(0xFF3D4B8A),
        tic = Color(0xFF236A6B),
        eoe = Color(0xFF179BAD),
        tsk = Color(0xFF671211),
        standalone = Color(0xFFA18978)
    ),
    warn = Color(0xFFC50707),
    green = Color(0xFF314629),
    red = Color(0xFF552D2D),
    upgrade = Color(0xFFcfb13a),
    warnText = Color(0xFFFB4135),
    table = TableColors(
        header = Color(0xFF293d2a),
        light = Color(0xFF455245),
        dark = Color(0xFF203021)
    ),
)

val LightColorScheme = CustomColors(
    l30 = Light30,
    l20 = Light20,
    l15 = Light15,
    l10 = Light10,
    m = Medium,
    d10 = Dark10,
    d15 = Dark15,
    d20 = Dark20,
    d30 = Dark30,
    fight = Color(0xFF8D181E),
    evade = Color(0xFF0D6813),
    background = Light30,
    darkText = Dark30,
    lightText = Dark10,
    taboo = Color(0xFF850083),
    divider = Light10,
    tarotText = DualDarkText,
    tarotInvertedText = DualDarkText,
    faction = FactionThemeColors(
        guardian = FactionColors(
            invertedText = GuardianDarkText,
            text = GuardianLightText,
            border = GuardianDarkText,
            background = GuardianLightText,
            darkBackground = Color(0xFF2b80c5),
            lightBackground = Color(0xFFd5e6f3)
        ),
        seeker = FactionColors(
            invertedText = SeekerDarkText,
            text = SeekerLightText,
            border = SeekerDarkText,
            background = Color(0xFFDB7C07),
            darkBackground = Color(0xFFdb7c07),
            lightBackground = Color(0xFFfbe6d4)
        ),
        rogue = FactionColors(
            invertedText = RogueDarkText,
            text = RogueLightText,
            border = RogueDarkText,
            background = Color(0xFF219428),
            darkBackground = Color(0xFF107116),
            lightBackground = Color(0xFFcfe3d0)
        ),
        mystic = FactionColors(
            invertedText = MysticDarkText,
            text = MysticLightText,
            border = MysticDarkText,
            background = Color(0xFF7554AB),
            darkBackground = Color(0xFF4331B9),
            lightBackground = Color(0xFFd9d6f1)
        ),
        survivor = FactionColors(
            invertedText = SurvivorDarkText,
            text = SurvivorLightText,
            border = SurvivorDarkText,
            background = Color(0xFFCC3038),
            darkBackground = Color(0xFFCC3038),
            lightBackground = Color(0xFFf5d6d7)
        ),
        neutral = FactionColors(
            invertedText = NeutralDarkText,
            text = NeutralLightText,
            border = NeutralBorder,
            background = Dark20,
            darkBackground = Color(0xFF444444),
            lightBackground = Color(0xFFe6e6e6)
        ),
        dual = FactionColors(
            invertedText = DualDarkText,
            text = DualLightText,
            border = DualLightText,
            background = Color(0xFFcfb13a),
            darkBackground = Color(0xFFc0c000),
            lightBackground = Color(0xFFf2f2cc)
        ),
        dead = FactionColors(
            invertedText = DeadDarkText,
            text = DeadLightText,
            border = DeadLightText,
            background = Color(0xFF704214),
            darkBackground = Color(0xFF5a3510),
            lightBackground = Color(0xFFd4c6b9)
        ),
        mythos = FactionColors(
            invertedText = MythosDarkText,
            text = MythosLightText,
            border = MythosLightText,
            background = Dark30,
            darkBackground = Color(0xFF000000),
            lightBackground = Color(0xFF000000)
        )
    ),
    skill = SkillThemeColors(
        willpower = Color(0xFF165385),
        intellect = Color(0xFF7A2D6C),
        combat = Color(0xFF8D181E),
        agility = Color(0xFF0D6813),
        wild = Color(0xFF635120)
    ),
    token = TokenColors(
        skull = Color(0xFF552D2D),
        cultist = Color(0xFF314629),
        tablet = Color(0xFF294146),
        elderThing = Color(0xFF442946),
        elderSign = Color(0xFF4477A1),
        autoFail = Color(0xFF7D1318),
        bless = Color(0xFF9D702A),
        curse = Color(0xFF3A2342),
        frost = Color(0xFF3D3A63)
    ),
    tokenFill = TokenFillColors(
        bless = Color(0xFFBFA640),
        curse = Color(0xFF7A2D6C)
    ),
    health = Color(0xFF8D181E),
    sanity = Color(0xFF165385),
    disableOverlay = Color(0xFFFBF299),
    campaign = CampaignColors(
        text = CampaignTextColors(
            setup = Color(0xFF128C60),
            interlude = Color(0xFF2D529A),
            resolution = Color(0xFFE75122)
        ),
        background = CampaignBackgroundColors(
            setup = Color(0x128C6033),
            interlude = Color(0x2D529A33),
            resolution = Color(0xE7512233)
        ),
        core = Color(0xFF00759C),
        dwl = Color(0xFF6D9548),
        ptc = Color(0xFF5B579C),
        tfa = Color(0xFFA45F9C),
        tcu = Color(0xFF593B5D),
        tde = Color(0xFF45559C),
        tic = Color(0xFF2A7D7F),
        eoe = Color(0xFF25B7CB),
        tsk = Color(0xFF8B1E1E),
        standalone = Color(0xFFAC9788)
    ),
    warn = Color(0xFFFB4135),
    green = Color(0xFF9AEC86),
    red = Color(0xFFFFDBD3),
    upgrade = Color(0xFFcfb13a),
    warnText = Color(0xFFC50707),
    table = TableColors(
        header = Color(0xFFa0dba3),
        light = Color(0xFFe3fce4),
        dark = Color(0xFFc7ebc9)
    ),
)

val LocalCustomColors = compositionLocalOf { LightColorScheme }