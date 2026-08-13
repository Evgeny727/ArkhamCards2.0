package com.arkhamcompanion.ui.cards.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arkhamcompanion.R
import com.arkhamcompanion.domain.enums.CardType
import com.arkhamcompanion.domain.model.cards.CardDetails
import com.arkhamcompanion.ui.icons.AppIcon
import com.arkhamcompanion.ui.theme.AppIconsFont
import com.arkhamcompanion.ui.theme.CustomTheme
import com.arkhamcompanion.ui.utils.appSp

@Composable
fun RowScope.CardDetailsMetaBlock(
    cardDetails: CardDetails,
    simpleBack: Boolean = false,
) {
    Column(
        modifier = Modifier.weight(1f),
    ) {
        cardDetails.run {
            if (type != CardType.Investigator) {
                CardDetailsTypeText(
                    typeName = typeName,
                    stage = stage,
                    subTypeName = subTypeName,
                    slot = slot,
                )
            }

            if (simpleBack) {
                backTraits?.let { traits ->
                    Text(
                        text = traits,
                        style = CustomTheme.typography.run { small + boldItalic }
                    )
                }
            } else {
                traits?.let { traits ->
                    Text(
                        text = traits,
                        style = CustomTheme.typography.run { small + boldItalic }
                    )
                }

                if (type != CardType.Investigator && (skillWillpower != null
                            || skillIntellect != null || skillCombat != null
                            || skillAgility != null || skillWild != null)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    CardDetailsSkillFlowRow(
                        isWeakness = subType != null,
                        skillWillpower = skillWillpower,
                        skillIntellect = skillIntellect,
                        skillCombat = skillCombat,
                        skillAgility = skillAgility,
                        skillWild = skillWild
                    )
                }

                if (type == CardType.Investigator) {
                    Spacer(modifier = Modifier.height(4.dp))

                    CardDetailsInvestigatorStatLine(
                        skillWillpower = skillWillpower,
                        skillIntellect = skillIntellect,
                        skillCombat = skillCombat,
                        skillAgility = skillAgility
                    )
                }

                if (type.isEnemyLike()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    CardDetailsEnemyStatBlock(
                        enemyFight = enemyFight,
                        enemyFightPerInvestigator = enemyFightPerInvestigator,
                        health = health,
                        healthPerInvestigator = healthPerInvestigator,
                        enemyEvade = enemyEvade,
                        enemyEvadePerInvestigator = enemyEvadePerInvestigator,
                        enemyDamage = enemyDamage,
                        enemyHorror = enemyHorror,
                    )
                }

                if (!type.isEnemyLike() && (health != null || sanity != null)) {
                    Spacer(modifier = Modifier.height(8.dp))

                    CardDetailsHealthSanityLine(
                        health = health,
                        sanity = sanity
                    )
                }

                if (type.isLocationLike() || type == CardType.Agenda || type == CardType.Act) {
                    DoomCluesShroudRow(
                        type = type,
                        doom = doom,
                        doomPerInvestigator = doomPerInvestigator,
                        shroud = shroud,
                        shroudPerInvestigator = shroudPerInvestigator,
                        clues = clues,
                        cluesFixed = cluesFixed
                    )
                }
            }
        }
    }
}

@Composable
private fun CardDetailsTypeText(
    typeName: String,
    stage: Int?,
    subTypeName: String?,
    slot: String?,
) {
    val text = buildString {
        stage?.let {
            append("$typeName $stage")
        } ?: append(typeName)

        subTypeName?.let {
            append(" · $subTypeName")
        }

        slot?.let {
            append(" · $slot")
        }
    }

    Text(
        text = text,
        style = CustomTheme.typography.run { small + bold },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CardDetailsSkillFlowRow(
    isWeakness: Boolean,
    skillWillpower: Int?,
    skillIntellect: Int?,
    skillCombat: Int?,
    skillAgility: Int?,
    skillWild: Int?
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        skillWillpower?.let {
            CardDetailsSkillIcons(
                skillCode = "willpower",
                skillCount = it,
                isWeakness = isWeakness
            )
        }

        skillIntellect?.let {
            CardDetailsSkillIcons(
                skillCode = "intellect",
                skillCount = it,
                isWeakness = isWeakness
            )
        }

        skillCombat?.let {
            CardDetailsSkillIcons(
                skillCode = "combat",
                skillCount = it,
                isWeakness = isWeakness
            )
        }

        skillAgility?.let {
            CardDetailsSkillIcons(
                skillCode = "agility",
                skillCount = it,
                isWeakness = isWeakness
            )
        }

        skillWild?.let {
            CardDetailsSkillIcons(
                skillCode = "wild",
                skillCount = it,
                isWeakness = isWeakness
            )
        }
    }
}

@Composable
private fun CardDetailsSkillIcons(
    skillCode: String,
    skillCount: Int,
    isWeakness: Boolean
) {
    repeat(skillCount) {
        Box(
            modifier = Modifier.background(
                color = CustomTheme.colors.l20,
                shape = CustomTheme.shapes.small
            ),
            contentAlignment = Alignment.Center
        ) {
            SkillIcon(
                skillCode = skillCode,
                iconSize = 24.appSp(CustomTheme.typography.scaleFactor),
                isWeakness = isWeakness,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun CardDetailsHealthSanityLine(
    health: Int?,
    sanity: Int?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            HealthSanityIcon(
                isHealth = true,
                iconSize = 28.appSp(CustomTheme.typography.scaleFactor),
                withBackground = false
            )
            HealthSanityNumericIcon(
                value = health,
                isHealth = true,
                iconSize = 24.appSp(CustomTheme.typography.scaleFactor)
            )
        }

        Box(contentAlignment = Alignment.TopCenter) {
            HealthSanityIcon(
                isHealth = false,
                iconSize = 28.appSp(CustomTheme.typography.scaleFactor),
                withBackground = false
            )
            HealthSanityNumericIcon(
                value = sanity,
                isHealth = false,
                iconSize = 24.appSp(CustomTheme.typography.scaleFactor)
            )
        }
    }
}

@Composable
private fun DoomCluesShroudRow(
    type: CardType,
    doom: Int?,
    doomPerInvestigator: Boolean,
    shroud: Int?,
    shroudPerInvestigator: Boolean,
    clues: Int?,
    cluesFixed: Boolean
) {
    val perInvestigatorIconText = buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontFamily = AppIconsFont,
                fontSize = 14.appSp(CustomTheme.typography.scaleFactor)
            )
        ) {
            append(AppIcon.PerInvestigator.glyph)
        }
    }
    val text = buildAnnotatedString {
        if (type == CardType.Agenda) {
            append(stringResource(
                R.string.doom_doom,
                numberValueToString(doom)
            ))

            if (doomPerInvestigator) {
                append(perInvestigatorIconText)
            }
        }
        if (type == CardType.Act) {
            append(stringResource(
                R.string.per_investigator_clues,
                numberValueToString(clues), "")
            )

            if ((clues ?: 0) > 0) {
                append(perInvestigatorIconText)
            }
        }
        if (type.isLocationLike()) {
            append(stringResource(R.string.shroud) + ": ")
            append(numberValueToString(shroud))
            if (shroudPerInvestigator) append(perInvestigatorIconText)
            append(". ")

            append(stringResource(
                R.string.per_investigator_clues,
                numberValueToString(clues), "")
            )
            if (!cluesFixed && (clues ?: 0) > 0) append(perInvestigatorIconText)
            append(". ")
        }
    }

    Text(
        text = text,
        style = CustomTheme.typography.small,
    )
}

@Stable
private fun numberValueToString(value: Int?): String = when (value) {
    null -> "-"
    -2 -> "X"
    else -> value.toString()
}