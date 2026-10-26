package com.example.dogbreeds.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreeds.data.repository.BreedRepository
import com.example.dogbreeds.domain.model.Breed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: BreedRepository,
    private val breedId: Int
) : ViewModel() {

    private val _breed = MutableStateFlow<Breed?>(null)
    val breed: StateFlow<Breed?> = _breed.asStateFlow()

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images.asStateFlow()

    init {
        viewModelScope.launch {
            _breed.value = repository.getBreedById(breedId)

        }
    }

    fun toggleFavorite() {
        val current = _breed.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(current)
            _breed.value = repository.getBreedById(breedId)
        }
    }
}