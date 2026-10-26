package com.example.dogbreeds.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreeds.data.repository.BreedRepository
import com.example.dogbreeds.domain.model.Breed
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: BreedRepository) : ViewModel() {

    val favorites: StateFlow<List<Breed>> = repository.favoriteBreeds
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(breed: Breed) {
        viewModelScope.launch {
            repository.toggleFavorite(breed)
        }
    }
}