package com.example.dogbreeds.data.remote

import com.example.dogbreeds.data.remote.dto.BreedDto
import com.example.dogbreeds.data.remote.dto.BreedImageDto

class RemoteDataSource(private val api: DogApiService) {
    suspend fun fetchBreeds(): List<BreedDto> = api.getBreeds()

    suspend fun fetchBreedImages(breedId: Int): List<BreedImageDto> =
        api.getBreedImages(breedId)
}