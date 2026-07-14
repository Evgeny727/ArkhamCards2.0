package com.arkhamcards.v2.data.mapper.domain.meta

import com.arkhamcards.v2.data.local.meta.TabooSetEntity
import com.arkhamcards.v2.domain.model.meta.TabooSet

internal fun TabooSetEntity.toDomain(): TabooSet = TabooSet(
    id = id,
    name = name.orEmpty(),
    date = date.orEmpty()
)