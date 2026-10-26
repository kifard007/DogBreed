package com.example.dogbreeds.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreeds.data.BreedRepository
import com.example.dogbreeds.domain.model.Breed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BreedListViewModel(private val repository: BreedRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    val breeds: StateFlow<List<Breed>> = combine(
        repository.allBreeds,
        _searchQuery
    ) { list, query ->
        if (query.isBlank()) list
        else list.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        println("DEBUG: refresh() called")
        viewModelScope.launch {
            println("DEBUG: refresh coroutine started")
            _isRefreshing.value = true
            try {
                repository.refreshBreeds()
                println("DEBUG: refresh SUCCESS")
            } catch (e: Exception) {
                println("DEBUG: refresh FAILED: ${e.message}")
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun toggleFavorite(breed: Breed) {
        viewModelScope.launch {
            repository.toggleFavorite(breed)
        }
    }
}