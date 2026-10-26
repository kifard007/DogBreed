package com.example.dogbreeds.domain.model

data class Breed(
    val id: Int,
    val name: String,
    val temperament: String,
    val origin: String?,
    val lifeSpan: String,
    val description: String?,
    val imageUrl: String,
    val isFavorite: Boolean
)