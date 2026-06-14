package com.arkhamcards.v2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arkhamcards.v2.data.local.cards.CardEntity
import com.arkhamcards.v2.data.local.cards.CardSubtypeEntity
import com.arkhamcards.v2.data.local.cards.CardTypeEntity
import com.arkhamcards.v2.data.local.dao.CardsDao
import com.arkhamcards.v2.data.local.dao.MetaDao
import com.arkhamcards.v2.data.local.meta.CycleEntity
import com.arkhamcards.v2.data.local.meta.EncounterSetEntity
import com.arkhamcards.v2.data.local.meta.FactionEntity
import com.arkhamcards.v2.data.local.meta.PackEntity
import com.arkhamcards.v2.data.local.meta.TabooSetEntity
import com.arkhamcards.v2.data.objects.JsonElementConverter
import com.arkhamcards.v2.data.objects.StringListConverter

@Database(entities = [CardEntity::class, CycleEntity::class, PackEntity::class, EncounterSetEntity::class, TabooSetEntity::class,
    FactionEntity::class, CardTypeEntity::class, CardSubtypeEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(JsonElementConverter::class, StringListConverter::class)
abstract class ArkhamDatabase : RoomDatabase() {
    abstract fun cardsDao(): CardsDao
    abstract fun metaDao(): MetaDao
}