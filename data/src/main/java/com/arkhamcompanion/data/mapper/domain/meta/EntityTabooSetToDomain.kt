package com.arkhamcompanion.data.mapper.domain.meta

import com.arkhamcompanion.data.local.meta.TabooSetEntity
import com.arkhamcompanion.domain.model.meta.TabooSet

internal fun TabooSetEntity.toDomain(): TabooSet = TabooSet(
    id = id,
    name = name.orEmpty(),
    date = date.orEmpty()
)