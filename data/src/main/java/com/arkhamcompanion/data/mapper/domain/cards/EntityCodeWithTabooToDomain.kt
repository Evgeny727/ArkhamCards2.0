package com.arkhamcompanion.data.mapper.domain.cards

import com.arkhamcompanion.data.local.cards.CodeWithTabooEntity
import com.arkhamcompanion.domain.model.cards.CodeWithTaboo
import kotlinx.collections.immutable.toImmutableList

fun List<CodeWithTabooEntity>.toDomain() = map { entity ->
    entity.toDomain()
}.toImmutableList()

fun CodeWithTabooEntity.toDomain() = CodeWithTaboo(
    code = code,
    tabooSetId = tabooSetId
)