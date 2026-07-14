package com.arkhamcards.v2.data.repository

import com.arkhamcards.v2.data.local.dao.MetaDao
import com.arkhamcards.v2.data.mapper.domain.meta.toDomain
import com.arkhamcards.v2.domain.model.meta.TabooSet
import com.arkhamcards.v2.domain.repository.MetaRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MetaRepositoryImpl @Inject constructor(
    private val metaDao: MetaDao
): MetaRepository {

    override fun getTaboos(): Flow<ImmutableList<TabooSet>> = metaDao.getTaboos().map { sets ->
        sets.map { it.toDomain() }.toImmutableList()
    }

}