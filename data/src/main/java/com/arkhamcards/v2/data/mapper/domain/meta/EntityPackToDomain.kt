package com.arkhamcards.v2.data.mapper.domain.meta

import com.arkhamcards.v2.data.local.meta.FullPackEntity
import com.arkhamcards.v2.domain.model.meta.Pack

internal fun FullPackEntity.toDomain(): Pack = Pack(
    code = code,
    name = name,
    cycleName = cycleName,
    position = position,
    cyclePosition = cyclePosition,
    official = official,
    reprint = reprint,
    chapter = chapter,
)