package com.arkhamcards.v2.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhamcards.v2.domain.model.settings.Collection
import com.arkhamcards.v2.domain.repository.CardsRepository
import com.arkhamcards.v2.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CardDetailsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val cardsRepository: CardsRepository
) : ViewModel() {

    val collectionFlow = userPreferencesRepository.collection.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Collection(persistentSetOf(), persistentSetOf())
    )

    val ignoreCollectionFlow = userPreferencesRepository.ignoreCollection.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val showFanmadeFlow = userPreferencesRepository.showFanmadeCards.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    fun getCardDetailsWithRelations(code: String, tabooSetId: Int?) =
        cardsRepository.getCardWithRelationsByCodeFlow(code, tabooSetId)
}