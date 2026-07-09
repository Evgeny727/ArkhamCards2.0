package com.arkhamcards.v2.domain.model.settings

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val cycles: ImmutableList<String>,
    val packs: ImmutableList<String>,
)