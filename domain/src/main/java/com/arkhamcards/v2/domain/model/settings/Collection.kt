package com.arkhamcards.v2.domain.model.settings

import com.arkhamcards.v2.domain.objects.ImmutableStringSetSerializer
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    @Serializable(with = ImmutableStringSetSerializer::class)
    val packs: ImmutableSet<String>,
    @Serializable(with = ImmutableStringSetSerializer::class)
    val reprintPacks: ImmutableSet<String>,
)