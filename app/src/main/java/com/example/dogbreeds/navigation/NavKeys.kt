package com.example.dogbreeds.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object BreedListRoute : NavKey

@Serializable
data class BreedDetailRoute(val breedId: Int) : NavKey

@Serializable
data object FavoritesRoute : NavKey