package com.arkhamcards.v2.domain.repository

import com.arkhamcards.v2.domain.model.meta.TabooSet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MetaRepository {

    fun getTaboos(): Flow<ImmutableList<TabooSet>>

}