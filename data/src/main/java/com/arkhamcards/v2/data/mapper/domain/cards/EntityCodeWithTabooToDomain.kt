package com.arkhamcards.v2.data.mapper.domain.cards

import com.arkhamcards.v2.data.local.cards.CodeWithTabooEntity
import com.arkhamcards.v2.domain.model.cards.CodeWithTaboo
import kotlinx.collections.immutable.toImmutableList

fun List<CodeWithTabooEntity>.toDomain() = map { entity ->
    entity.toDomain()
}.toImmutableList()

fun CodeWithTabooEntity.toDomain() = CodeWithTaboo(
    code = code,
    tabooSetId = tabooSetId
)